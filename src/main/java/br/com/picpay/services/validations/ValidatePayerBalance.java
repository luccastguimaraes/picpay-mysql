package br.com.picpay.services.validations;

import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.dto.TransactionDTO;
import br.com.picpay.services.CommonUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidatePayerBalance implements Validation{

   @Autowired
   private CommonUserService commonUserService;
   
   @Override
   public void validate(TransactionDTO transactionDTO) throws Exception {
      CommonUser payer = this.commonUserService.findUserById(transactionDTO.payerId());
      validateBalance(payer, transactionDTO.amountTransferred());
   }

   private void validateBalance (CommonUser payer, BigDecimal amountTransferred) throws Exception {
      if(payer.getBalance().compareTo(BigDecimal.ZERO) <= 0 || payer.getBalance().compareTo(amountTransferred) < 0) {
         throw new Exception("Saldo insuficiente para realizar a transação.");
      }
   }
}
