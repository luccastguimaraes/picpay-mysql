package br.com.picpay.services;

import br.com.picpay.domain.user.CommonUser;
import br.com.picpay.dto.DadosListagemUser;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


   @Override
   public DadosListagemUser createUser(UserDTO userDTO) throws Exception {
      CommonUser newUser = new CommonUser(
            userDTO.firstName(),
            userDTO.lastName(),
            userDTO.document(),
            userDTO.email(),
            passwordEncoder.encode(userDTO.password()),
            userDTO.balance()
      );
      save(newUser);
      return toTransform(newUser);
   }
}
