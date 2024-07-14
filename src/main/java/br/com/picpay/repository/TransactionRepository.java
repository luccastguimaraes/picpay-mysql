package br.com.picpay.repository;

import br.com.picpay.domain.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   Page<Transaction> findByPayerId(Long id, Pageable pageable);
   Page<Transaction> findByPayeeId(Long id, Pageable pageable);
}
