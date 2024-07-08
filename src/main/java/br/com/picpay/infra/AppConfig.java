package br.com.picpay.infra;

import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.domain.user.MerchantUser;
import br.com.picpay.repository.UserRepository;
import br.com.picpay.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }

   @Bean
   public CommonUserService commonUserService(UserRepository repository) {
      return new CommonUserService(repository);
   }

   @Bean
   public MerchantUserService merchantUserService(UserRepository repository) {
      return new MerchantUserService(repository);
   }
}
