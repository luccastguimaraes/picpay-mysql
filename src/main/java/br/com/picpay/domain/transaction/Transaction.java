package br.com.picpay.domain.transaction;


import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private BigDecimal amount;
   @ManyToOne
   @JoinColumn(name = "payer_id")
   @JsonBackReference
   private CommonUser payer;
   @ManyToOne
   @JoinColumn(name = "payee_id")
   @JsonBackReference
   private User payee;
   private LocalDateTime transactionTime;

}
