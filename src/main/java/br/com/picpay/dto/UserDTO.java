package br.com.picpay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UserDTO(
      @NotBlank
      String firstName,
      @NotBlank
      String lastName,
      @NotBlank
      String document,
      @NotBlank @Email
      String email,
      @NotBlank
      String password,
      @NotNull
      BigDecimal balance
) {}
