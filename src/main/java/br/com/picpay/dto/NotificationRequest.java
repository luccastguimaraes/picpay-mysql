package br.com.picpay.dto;


public record NotificationRequest(String email, String status, NotificationData data) {
   public record NotificationData(String message){}
}
