package br.com.picpay.controller;

import br.com.picpay.security.Credencials;
import br.com.picpay.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

   @Autowired
   private AuthenticationManager authenticationManager;
   @Autowired
   private TokenService tokenService;

   @PostMapping("/login")
   public ResponseEntity login(@RequestBody @Valid Credencials credencials) {
      Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credencials.email(), credencials.password())
      );
      var token = tokenService.gerarToken(authenticate);

      return ResponseEntity.ok(token);
   }
}
