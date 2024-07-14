package br.com.picpay.domain.user;

import br.com.picpay.domain.transaction.Transaction;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "common_users")
@Getter
public class CommonUser extends User {

   @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY)
   private List<Transaction> transactionsAsPayer = new ArrayList<>();

   public CommonUser(String firstName, String lastName, String document, String email, String password, BigDecimal balance) {
      super(firstName, lastName, document, email, password, balance, UserType.COMMON);
   }

   public CommonUser() {}


   @Override
   public void addTransaction(Transaction transaction) throws Exception {
      if (transaction.getPayee().id.equals(this.id)){
         this.transactionsAsPayee.add(transaction);
      } else if (transaction.getPayer().id.equals(this.id)){
         this.transactionsAsPayer.add(transaction);
      } else {
         throw new Exception("Tried to save a transaction in the add Transaction method of the Common class");
      }
   }

   public void transfer(BigDecimal amountTransferred){
      this.balance = this.balance.subtract(amountTransferred);
   }
}
