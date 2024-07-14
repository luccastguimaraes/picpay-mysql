package br.com.picpay.services;

import br.com.picpay.domain.user.User;
import br.com.picpay.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationSerivce {

   @Autowired
   private RestTemplate restTemplate;
   @Value("${mock.send.notification}")
   private String mockSendNotification;



   public void sendNotification(String email, String status, String message) throws Exception {
      try{
         NotificationRequest.NotificationData data = new NotificationRequest.NotificationData(message);
         NotificationRequest request = new NotificationRequest(email, status, data);
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity<NotificationRequest> requestHttpEntity = new HttpEntity<>(request, headers);
         restTemplate.postForEntity(mockSendNotification, requestHttpEntity, Void.class);
         System.out.println(requestHttpEntity.getBody().toString());
      } catch (RestClientException e) {
         throw new Exception("Failed to send notification: " + e.getMessage(), e);
      }
   }
}
