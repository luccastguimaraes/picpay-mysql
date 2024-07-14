package br.com.picpay.domain.user;

import br.com.picpay.domain.transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@EqualsAndHashCode(of = "id")
public abstract class User implements UserDetails {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   protected Long id;
   protected String firstName;
   protected String lastName;
   @Column(unique = true)
   protected String document;
   @Column(unique = true)
   protected String email;
   @Setter
   protected String password;
   protected BigDecimal balance;
   @Enumerated(EnumType.STRING)
   protected UserType userType;

   @OneToMany(mappedBy = "payee", fetch = FetchType.LAZY)
   protected List<Transaction> transactionsAsPayee = new ArrayList<>();

   // protected para garantir que seja instanciada apenas atraves de subclass concretas
   protected User(String firstName, String lastName, String document, String email, String password, BigDecimal balance, UserType userType) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.document = document;
      this.email = email;
      this.password = password;
      this.balance = balance;
      this.userType = userType;
   }

   protected User() {
   }


   public void receive(BigDecimal amountTransferred) {
      this.balance = this.balance.add(amountTransferred);
   }

   public abstract void addTransaction(Transaction transaction) throws Exception;


   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_USER"));
   }

   @Override
   public String getUsername() {
      return this.email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
