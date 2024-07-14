package br.com.picpay.repository;


import br.com.picpay.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByDocument(String document);

   UserDetails findByEmail(String email);
}
