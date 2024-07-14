package br.com.picpay.services;

import br.com.picpay.domain.user.User;
import br.com.picpay.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFactory {

   private final UserRepository repository;
   private final CommonUserService commonUserService;
   private final MerchantUserService merchantUserService;

   @Autowired
   public UserServiceFactory(UserRepository repository, CommonUserService commonUserService, MerchantUserService merchantUserService) {
      this.repository = repository;
      this.commonUserService = commonUserService;
      this.merchantUserService = merchantUserService;
   }



   public UserService getUserService(Long id){
      User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("CommonUser not found with ID: " + id));

      if ("COMMON".equalsIgnoreCase(user.getUserType().toString())){
         return this.commonUserService;
      } else if ("MERCHANT".equalsIgnoreCase(user.getUserType().toString())) {
         return this.merchantUserService;
      } else {
         throw new IllegalArgumentException("Tipo de usuário não suportado: " + user.getUserType());
      }
   }
}
