package br.com.picpay.services;

import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CommonUserService extends UserService<CommonUser> {


   @Autowired
   public CommonUserService(UserRepository repository) {
      super(repository);
   }

   @Override
   public CommonUser findUserById(Long id) {
      return repository.findById(id)
            .filter(user -> user instanceof CommonUser)
            .map(user -> (CommonUser) user)
            .orElseThrow(() -> new EntityNotFoundException("CommonUser not found with ID: " + id));
   }

   /*
    parece redudante mas no meu pensamento
    é para evitar que seja realizado transaction sem necessidade
    por exemplo de valor 0
    */
   public void validateBalance (CommonUser user, BigDecimal amountTransferred) throws Exception {
      if(user.getBalance().compareTo(BigDecimal.ZERO) <= 0 || user.getBalance().compareTo(amountTransferred) < 0) {
         throw new Exception("Saldo insuficiente para realizar a transação.");
      }
   }


   @Override
   public CommonUser createUser(UserDTO userDTO) throws Exception {
      CommonUser newUser = new CommonUser(
            userDTO.firstName(),
            userDTO.lastName(),
            userDTO.document(),
            userDTO.email(),
            userDTO.password(),
            userDTO.balance()
      );
      save(newUser);
      return newUser;
   }
}
