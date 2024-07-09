package br.com.picpay.services;

import br.com.picpay.domain.transaction.Transaction;
import br.com.picpay.domain.user.User;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.repository.UserRepository;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;

import java.util.List;

/*
   A ideia aqui é criar uma estrutura que permita funcionalidades comuns e
   específicas para cada tipo de User, permitindo reutilizar a mesma classe UserService
   , por meio de UserRepository desde que T seja uma subclass de User.
 */
public abstract class UserService<T extends User> {


   protected UserRepository repository;

   public UserService(UserRepository repository) {
      this.repository = repository;
   }

   public abstract User findUserById(Long id);

   public void save(User user) throws Exception {
      try {
         this.repository.save(user);
      } catch (ConstraintViolationException e) {
         throw new IllegalArgumentException("Erro de validação ao salvar usuário: " + e.getMessage());
      } catch (DataAccessException e) {
         throw new RuntimeException("Erro ao acessar dados ao salvar usuário: " + e.getMessage());
      } catch (TransactionException e) {
         throw new RuntimeException("Erro na transação ao salvar usuário: " + e.getMessage());
      } catch (Exception e) {
         throw new Exception("Erro na transação ao salvar usuário: ");
      }
   }

   public abstract User createUser(UserDTO userDTO) throws Exception;

   public void saveTransaction(Transaction transaction) throws Exception {
      User user = transaction.getPayer();
      user.addTransaction(transaction);
      this.save(user);
      user = transaction.getPayee();
      user.addTransaction(transaction);
      this.save(user);

   }

   public List<User> getAllUsers() {
      return this.repository.findAll();
   }

   public List<Transaction> getTransactionsForUser(Long id){
      return findUserById(id).getAllTransactions();
   }
}