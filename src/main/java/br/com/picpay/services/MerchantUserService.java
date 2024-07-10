package br.com.picpay.services;

import br.com.picpay.domain.user.MerchantUser;
import br.com.picpay.dto.DadosListagemUser;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserService extends UserService<MerchantUser> {

   @Autowired
   public MerchantUserService(UserRepository repository) {
      super(repository);
   }

   @Override
   public MerchantUser findUserById(Long id) {
      return repository.findById(id)
            .filter(user -> user instanceof MerchantUser)
            .map(user -> (MerchantUser) user)
            .orElseThrow(() -> new RuntimeException("MerchantUser not found with ID: " + id));
   }


   @Override
   public DadosListagemUser createUser(UserDTO userDTO) throws Exception {
      MerchantUser newUser = new MerchantUser(
            userDTO.firstName(),
            userDTO.lastName(),
            userDTO.document(),
            userDTO.email(),
            userDTO.password(),
            userDTO.balance()
      );
      save(newUser);
      return toTransform(newUser);
   }
}
