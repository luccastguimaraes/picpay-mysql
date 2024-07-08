package br.com.picpay.domain.user;

import br.com.picpay.domain.transaction.Transaction;
import br.com.picpay.dto.TransactionDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@EqualsAndHashCode(of = "id")
public abstract class User {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   protected Long id;
   protected String firstName;
   protected String lastName;
   @Column(unique = true)
   protected String document;
   @Column(unique = true)
   protected String email;
   protected String password;
   protected BigDecimal balance;
   @Enumerated(EnumType.STRING)
   protected UserType userType;

   @OneToMany(mappedBy = "payee")
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

   protected User(UserType userType) {
      this.userType = userType;
   }


   public void receive(BigDecimal amountTransferred) {
      this.balance = this.balance.add(amountTransferred);
   }

   public abstract List<Transaction> getAllTransactions();

   public abstract void addTransaction(Transaction transaction) throws Exception;

   private void setTransactionsAsPayee(List<Transaction> transactionsAsPayee) {
      this.transactionsAsPayee = transactionsAsPayee;
   }

   private List<Transaction> getTransactionsAsPayee() {
      return transactionsAsPayee;
   }
}
