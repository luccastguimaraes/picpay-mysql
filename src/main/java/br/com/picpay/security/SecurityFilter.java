package br.com.picpay.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

   @Autowired
   private AuthenticationService authenticationService;
   @Autowired
   private TokenService tokenService;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String token = request.getHeader("Authorization");
      if (true) {
         filterChain.doFilter(request, response);
      } else {
         if (!token.startsWith("Bearer ")) {
            token = null;
         } else {
            token = token.substring(7, token.length());
         }
         boolean valido = tokenService.isTokenValido(token);
         if (valido) {
            autenticarCliente(token);
         }
         filterChain.doFilter(request, response);
      }
   }

   private void autenticarCliente(String token) {
      if (token!=null) {
         String email = tokenService.getUsername(token);
         UserDetails userDetails = authenticationService.loadUserByUsername(email);
         var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
         SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
         throw new NoSuchElementException("Usuario nao presente no metodo autenticaCliente da class AuthnenticationTokenFIlter");
      }
   }
}
