package br.com.picpay.services;

import br.com.picpay.domain.transaction.Transaction;
import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.domain.user.User;
import br.com.picpay.dto.DadosUserTransactionsDTO;
import br.com.picpay.dto.TransactionDTO;
import br.com.picpay.repository.TransactionRepository;
import br.com.picpay.services.validations.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


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
   private List<Validation> validations;

   public static final String STATUS_SUCCESS = "success";

   public static final String STATUS_FAIL = "fail";


   /*
         Para que todas as operações dentro deste método devem ser tratadas como uma transação única.
         Se ocorrer uma exceção (Exception ou subclasse dela),
         a transação será revertida (rollback) automaticamente, garantindo a integridade dos dados.
       */

   @Transactional(rollbackFor = Exception.class)
   public DadosUserTransactionsDTO startTransaction(TransactionDTO transactionDTO) throws Exception {
      validations.forEach(validation -> {
         try {
            validation.validate(transactionDTO);
         } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
         }
      });
      CommonUser payer = this.commonUserService.findUserById(transactionDTO.payerId());
      User payee = this.userServiceFactory.getUserService(transactionDTO.payeeId()).findUserById(transactionDTO.payeeId());
      Transaction transaction = createTransaction(payer, payee, transactionDTO.amountTransferred());
      return new DadosUserTransactionsDTO(transaction);
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
         status = STATUS_SUCCESS;
         msgPayer = "You have made a transfer of $" + amountTransferred;
         msgPayee = "You have received a transfer of $" + amountTransferred;
         return transaction;
      } catch (Exception e) {
         throw new Exception("Create Transaction Error " + e.getMessage());
      } finally {
         try {
            sendNotification(payer.getEmail(), status, msgPayer);
            sendNotification(payee.getEmail(), status, msgPayee);
         } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
         }
      }
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

   public Page<DadosUserTransactionsDTO> getPayerTransactionsByUserId(Long id, Pageable pageable) {
      return this.transactionRepository.findByPayerId(id, pageable)
            .map(DadosUserTransactionsDTO::new);
   }

   public Page<DadosUserTransactionsDTO> getPayeeTransactionsByUserId(Long id, Pageable pageable) {
      return this.transactionRepository.findByPayeeId(id, pageable)
            .map(DadosUserTransactionsDTO::new);
   }
}
