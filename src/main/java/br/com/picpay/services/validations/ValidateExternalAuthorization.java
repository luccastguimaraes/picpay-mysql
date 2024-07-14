package br.com.picpay.services.validations;

import br.com.picpay.dto.AuthorizationResponse;
import br.com.picpay.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static br.com.picpay.services.TransactionService.STATUS_SUCCESS;

@Component
public class ValidateExternalAuthorization implements Validation{

   @Autowired
   private RestTemplate restTemplate;
   @Value("${mock.autorizador}")
   private String mockAutorizadorUrl;

   @Override
   public void validate(TransactionDTO transactionDTO) throws Exception {
      AuthorizationResponse authorizationResponse = restTemplate.getForObject(
            mockAutorizadorUrl,
            AuthorizationResponse.class
      );
      if (authorizationResponse==null) throw new Exception("authorizationResponse is null");
      boolean isSuccess = STATUS_SUCCESS.equalsIgnoreCase(authorizationResponse.status());
      boolean isTrue = "true".equalsIgnoreCase(String.valueOf(authorizationResponse.data().authorization()));
      if (!isSuccess) throw new Exception("authorizationResponse.status is fail, status: " + authorizationResponse.status());
      if (!isTrue) throw new Exception("authorizationResponse.authorization  is false, authorization: " + authorizationResponse.data().authorization());

   }
}
