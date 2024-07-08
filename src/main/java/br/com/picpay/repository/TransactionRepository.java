package br.com.picpay.repository;

import br.com.picpay.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
