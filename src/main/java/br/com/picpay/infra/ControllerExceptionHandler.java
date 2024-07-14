package br.com.picpay.infra;

import br.com.picpay.dto.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

   @ExceptionHandler(DataIntegrityViolationException.class)
   public ResponseEntity duplicateThreatEntry(DataIntegrityViolationException exception){
      ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getCause(), exception.getMessage());
      return ResponseEntity.badRequest().body(exceptionDTO);
   }

   @ExceptionHandler(EntityNotFoundException.class)
   public ResponseEntity threat404(EntityNotFoundException exception){
      ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getCause(), exception.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity threatGeneralException(Exception exception){
      ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getCause(), exception.getMessage());
      return ResponseEntity.badRequest().body(exceptionDTO);
   }
}
