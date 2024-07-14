package br.com.picpay.dto;

import br.com.picpay.domain.user.User;

public record DadosUserDTO (String userType, String firstName, String lastName, String document) {
   public DadosUserDTO (User u){
      this(u.getUserType().name(), u.getFirstName(), u.getLastName(), u.getDocument());
   }
}
