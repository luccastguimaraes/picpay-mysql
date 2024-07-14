package br.com.picpay.services;

import br.com.picpay.domain.transaction.Transaction;
import br.com.picpay.domain.user.User;
import br.com.picpay.dto.DadosListagemUser;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*
   A ideia aqui é criar uma estrutura que permita funcionalidades comuns e
   específicas para cada tipo de User, permitindo reutilizar a mesma classe UserService
   , por meio de UserRepository desde que T seja uma subclass de User.
 */
@Component
public abstract class UserService<T extends User> {

   @Autowired
   protected PasswordEncoder passwordEncoder;
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

   public abstract DadosListagemUser createUser(UserDTO userDTO) throws Exception;

   public void saveTransaction(Transaction transaction) throws Exception {
      User user = transaction.getPayer();
      user.addTransaction(transaction);
      this.save(user);
      user = transaction.getPayee();
      user.addTransaction(transaction);
      this.save(user);

   }

   public Page<DadosListagemUser> getAllUsers(Pageable pageable) {
      return this.repository.findAll(pageable).map(DadosListagemUser::new);
   }

   public DadosListagemUser getUser(Long id) {
      User user = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Find user by id not found"));
      return toTransform(user);

   }

   public DadosListagemUser toTransform(User user) {
      return new DadosListagemUser(user);
   }

}