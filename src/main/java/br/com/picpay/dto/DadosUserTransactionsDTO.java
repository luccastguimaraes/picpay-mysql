package br.com.picpay.dto;

import br.com.picpay.domain.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosUserTransactionsDTO(Long id, BigDecimal amount, String payer, String payee, LocalDateTime transactionTime) {
   public DadosUserTransactionsDTO (Transaction t) {
      this(t.getId(), t.getAmount(),
            t.getPayer().getFirstName() + " " + t.getPayer().getLastName(),
            t.getPayee().getFirstName() + " " + t.getPayee().getLastName(),
            t.getTransactionTime());
   }
}
