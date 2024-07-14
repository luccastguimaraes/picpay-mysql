package br.com.picpay.security;

import br.com.picpay.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

   private final Algorithm algorithm;
   private final long expirationTimeMillis;

   public TokenService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationTimeMillis) {
      this.algorithm = Algorithm.HMAC256(secret);
      this.expirationTimeMillis = expirationTimeMillis;
   }

   public String gerarToken(Authentication authenticate) {
      User user = (User) authenticate.getPrincipal();
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + expirationTimeMillis);
      return JWT.create()
            .withSubject(user.getUsername())
            .withIssuer("auth0")
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .sign(algorithm);
   }

   public boolean isTokenValido(String token) {
      try {
         DecodedJWT jwt = JWT.require(algorithm).withIssuer("auth0").build().verify(token);
         return true;
      } catch (JWTVerificationException exception) {
         return false;
      }
   }

   public String getUsername(String token) {
      try {
         return JWT.decode(token).getSubject();
      } catch (JWTDecodeException exception) {
         throw new JWTDecodeException(exception.getMessage());
      }
   }
}
