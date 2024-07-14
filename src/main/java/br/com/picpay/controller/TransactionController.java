package br.com.picpay.controller;

import br.com.picpay.dto.DadosUserTransactionsDTO;
import br.com.picpay.dto.TransactionDTO;
import br.com.picpay.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TransactionController {

   @Autowired
   private TransactionService transactionService;


   @PostMapping("/transfer")
   public ResponseEntity<DadosUserTransactionsDTO> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) throws Exception {
      DadosUserTransactionsDTO transaction = this.transactionService.startTransaction(transactionDTO);
      return ResponseEntity.ok().body(transaction);
   }


   @GetMapping("/payerTransactions/{id}")
   public ResponseEntity<?> getPayerTransactions(@PathVariable Long id, Pageable pageable){
      try {
         var payerTransactions = transactionService.getPayerTransactionsByUserId(id, pageable);
         return ResponseEntity.ok().body(payerTransactions);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping("/payeeTransactions/{id}")
   public ResponseEntity<?> getPayeeTransactions(@PathVariable Long id, Pageable pageable){
      try {
         var payeeTransactions = transactionService.getPayeeTransactionsByUserId(id, pageable);
         return ResponseEntity.ok().body(payeeTransactions);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }
}
