package br.com.picpay.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(
      @NotNull BigDecimal amountTransferred,
      @NotNull Long payerId,
      @NotNull Long payeeId
) {}
