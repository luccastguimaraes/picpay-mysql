package br.com.picpay.dto;

import br.com.picpay.domain.user.User;
import br.com.picpay.domain.user.UserType;

import java.math.BigDecimal;

public record DadosListagemUser (Long id, UserType userType, String firstName, String lastName, String document, String email, BigDecimal balance) {
   public DadosListagemUser(User u) {
      this(u.getId(), u.getUserType(), u.getFirstName(), u.getLastName(), u.getDocument(), u.getEmail(), u.getBalance());
   }
}
