package br.com.picpay.domain.transaction;


import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "payer_id")
   @JsonIgnore
   private CommonUser payer;
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "payee_id")
   @JsonIgnore
   private User payee;
   @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
   private LocalDateTime transactionTime;

}
