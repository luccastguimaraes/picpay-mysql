package br.com.picpay.dto;

public record ExceptionDTO(Throwable field, String message) {
}
