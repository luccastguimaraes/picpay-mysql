package br.com.picpay.dto;

import br.com.picpay.domain.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosUserTransactionsDTO(Long id, BigDecimal amount, LocalDateTime transactionTime,
                                       DadosUserDTO payer, DadosUserDTO payee) {
   public DadosUserTransactionsDTO (Transaction t) {
      this(t.getId(), t.getAmount(), t.getTransactionTime(),
            new DadosUserDTO(t.getPayer()),
            new DadosUserDTO(t.getPayee()));
   }
}
