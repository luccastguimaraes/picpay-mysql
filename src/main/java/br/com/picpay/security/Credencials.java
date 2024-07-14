package br.com.picpay.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Credencials(@NotBlank @Email String email, @NotBlank String password) {
}
