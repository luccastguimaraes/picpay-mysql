package br.com.picpay.domain.user;


import br.com.picpay.domain.transaction.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "merchant_users")
@Getter
public class MerchantUser extends User{

   @Column(unique = true)
   private String cnpj;

   public MerchantUser(String firstName, String lastName, String document, String email, String password, BigDecimal balance, String cnpj) {
      super(firstName, lastName, document, email, password, balance, UserType.MERCHANT);
      this.cnpj = cnpj;
   }

   public MerchantUser() {
      super(UserType.MERCHANT);
   }

   @Override
   public List<Transaction> getAllTransactions() {
      return this.transactionsAsPayee;
   }

   @Override
   public void addTransaction(Transaction transaction) throws Exception {
      if (transaction.getPayee().id.equals(this.id)){
         this.transactionsAsPayee.add(transaction);
      } else {
         throw new Exception("ERRO no metodo addTranscation da classe MerchantUser");
      }
   }
}
