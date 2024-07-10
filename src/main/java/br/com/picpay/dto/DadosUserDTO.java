package br.com.picpay.dto;

import br.com.picpay.domain.user.User;
import br.com.picpay.domain.user.UserType;

public record DadosUserDTO (Long id, UserType userType, String firstName) {
   public DadosUserDTO (User u){
      this(u.getId(), u.getUserType(), u.getFirstName());
   }
}
