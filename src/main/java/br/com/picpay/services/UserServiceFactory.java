package br.com.picpay.services;

import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.domain.user.MerchantUser;
import br.com.picpay.domain.user.User;
import br.com.picpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFactory {

   @Autowired
   private CommonUserService commonUserService;
   @Autowired
   private MerchantUserService merchantUserService;


   public UserService<? extends User> getUserService(User user){
      if (user instanceof CommonUser){
         return this.commonUserService;
      } else if (user instanceof MerchantUser) {
         return this.merchantUserService;
      } else {
         throw new IllegalArgumentException("Tipo de usuário não suportado: " + user.getClass());
      }
   }
}
