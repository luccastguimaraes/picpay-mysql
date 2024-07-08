package br.com.picpay.controller;

import br.com.picpay.domain.transaction.Transaction;
import br.com.picpay.dto.TransactionDTO;
import br.com.picpay.services.NotificationSerivce;
import br.com.picpay.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

   @Autowired
   private TransactionService transactionService;


   @PostMapping("/transfer")
   public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) throws Exception {
      Transaction transaction = this.transactionService.startTransaction(transactionDTO);
      return ResponseEntity.ok(transaction);
   }
}
