package br.com.picpay.services;

import br.com.picpay.domain.transaction.Transaction;
import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.domain.user.User;
import br.com.picpay.dto.AuthorizationResponse;
import br.com.picpay.dto.TransactionDTO;
import br.com.picpay.repository.TransactionRepository;
import br.com.picpay.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class TransactionService {

   @Autowired
   private CommonUserService commonUserService;

   @Autowired
   private UserServiceFactory userServiceFactory;

   @Autowired
   private TransactionRepository transactionRepository;

   @Autowired
   private NotificationSerivce notificationSerivce;

   @Autowired
   private RestTemplate restTemplate;

   @Value("${mock.autorizador}")
   private String mockAutorizadorUrl;

   public static final String STATUS_SUCCESS = "success";

   public static final String STATUS_FAIL = "fail";


   /*
         Para que todas as operações dentro deste método devem ser tratadas como uma transação única.
         Se ocorrer uma exceção (Exception ou subclasse dela),
         a transação será revertida (rollback) automaticamente, garantindo a integridade dos dados.
       */

   @Transactional(rollbackFor = Exception.class)
   public Transaction startTransaction(TransactionDTO transactionDTO) throws Exception {
      CommonUser payer = validatePayer(transactionDTO);
      BigDecimal amountTransferred = transactionDTO.amountTransferred();
      validatePayerBalancer(payer, amountTransferred);
      var payee = this.userServiceFactory.getUserService(transactionDTO.payeeId()).findUserById(transactionDTO.payeeId());

      return externalAuthorizer(payer, payee, amountTransferred);
   }

   private void sendNotification(String email, String status, String message) throws Exception {
      if (STATUS_SUCCESS.equalsIgnoreCase(status)) {
         this.notificationSerivce.sendNotification(
               email,
               status,
               message
         );
      }
   }

   private void validatePayerBalancer(CommonUser payer, BigDecimal amountTransferred) throws Exception {
      this.commonUserService.validateBalance(payer, amountTransferred);
   }

   private CommonUser validatePayer(TransactionDTO transactionDTO) throws Exception {
      if (transactionDTO.payerId().equals(transactionDTO.payeeId())) {
         throw new Exception("It is not permitted to transfer to the same person");
      }
      return this.commonUserService.findUserById(transactionDTO.payerId());

   }

   private Transaction externalAuthorizer(CommonUser payer, User payee, BigDecimal amountTransferred) throws Exception {
      AuthorizationResponse authorizationResponse = restTemplate.getForObject(
            mockAutorizadorUrl,
            AuthorizationResponse.class
      );
      boolean criterion1 = false;
      boolean criterion2 = false;
      if (authorizationResponse!=null) {
         criterion1 = STATUS_SUCCESS.equalsIgnoreCase(authorizationResponse.status());
         criterion2 = "true".equalsIgnoreCase(String.valueOf(authorizationResponse.data().authorization()));
         if (criterion1 && criterion2) {
            return this.createTransaction(payer, payee, amountTransferred);
         } else {
            throw new Exception("Authorization is false");
         }
      } else {
         throw new IllegalArgumentException("External Authorization Response is null");
      }
   }


   private Transaction createTransaction(CommonUser payer, User payee, BigDecimal amountTransferred) throws Exception {
      String status = STATUS_FAIL;
      String msgPayer = "Transaction Error";
      String msgPayee = "Transaction Error";
      try {
         Transaction transaction = new Transaction();
         transaction.setPayer(payer);
         transaction.setPayee(payee);
         transaction.setAmount(amountTransferred);
         transaction.setTransactionTime(LocalDateTime.now());
         payer.transfer(amountTransferred);
         payee.receive(amountTransferred);
         transactionRepository.save(transaction);
         commonUserService.saveTransaction(transaction); // salva os dois User automaticamente, em teoria.
         //commonUserService.save(payer);
         //userRepository.save(payee);
         status = STATUS_SUCCESS;
         msgPayer = "You have made a transfer of $" + amountTransferred;
         msgPayee = "You have received a transfer of $" + amountTransferred;
         return transaction;
      } catch (Exception e) {
         throw new Exception("Create Transaction Error", e);
      } finally {
         try {
            sendNotification(payer.getEmail(), status, msgPayer);
            sendNotification(payee.getEmail(), status, msgPayee);
         } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
         }
      }
   }


}
