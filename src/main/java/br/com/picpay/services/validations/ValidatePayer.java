package br.com.picpay.services.validations;

import br.com.picpay.dto.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidatePayer implements Validation{

   @Override
   public void validate(TransactionDTO transactionDTO) throws Exception {
      if (transactionDTO.payerId().equals(transactionDTO.payeeId())) {
         throw new Exception("It is not permitted to transfer to the same person");
      }
   }
}
