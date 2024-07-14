package br.com.picpay.domain.user;


import br.com.picpay.domain.transaction.Transaction;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "merchant_users")
@Getter
public class MerchantUser extends User{

   public MerchantUser(String firstName, String lastName, String document, String email, String password, BigDecimal balance) {
      super(firstName, lastName, document, email, password, balance, UserType.MERCHANT);
   }

   public MerchantUser() {}

   @Override
   public void addTransaction(Transaction transaction) throws Exception {
      if (transaction.getPayee().id.equals(this.id)){
         this.transactionsAsPayee.add(transaction);
      } else {
         throw new Exception("Tried to save a transaction in the add Transaction method of the Merchant class");
      }
   }
}
