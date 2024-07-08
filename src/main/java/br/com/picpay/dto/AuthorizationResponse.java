package br.com.picpay.dto;

public record AuthorizationResponse(String status, AuthorizationData data) {
   public record AuthorizationData(boolean authorization){}
}
