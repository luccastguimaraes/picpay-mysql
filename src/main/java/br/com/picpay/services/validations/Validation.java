package br.com.picpay.services.validations;

import br.com.picpay.dto.TransactionDTO;

public interface Validation {

   void validate(TransactionDTO transactionDTO) throws Exception;
}
