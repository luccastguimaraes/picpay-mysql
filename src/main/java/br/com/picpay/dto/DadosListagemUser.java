package br.com.picpay.dto;

import br.com.picpay.domain.user.User;

import java.math.BigDecimal;

public record DadosListagemUser (Long id, String userType, String name , String document, String email, BigDecimal balance) {
   public DadosListagemUser(User u) {
      this(u.getId(), u.getUserType().name(), u.getFirstName() + " " + u.getLastName(), u.getDocument(), u.getEmail(), u.getBalance());
   }
}
