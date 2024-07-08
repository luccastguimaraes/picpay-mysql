package br.com.picpay.controller;

import br.com.picpay.domain.user.User;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.repository.UserRepository;
import br.com.picpay.services.CommonUserService;
import br.com.picpay.services.MerchantUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

   @Autowired
   private CommonUserService commonUserService;
   @Autowired
   private MerchantUserService merchantUserService;
   @Autowired
   private UserRepository repository;

   @PostMapping("/common")
   public ResponseEntity<User> createCommonUser(@RequestBody @Valid UserDTO userDTO) throws Exception {
      User newUser = commonUserService.createUser(userDTO);
      return ResponseEntity.ok(newUser);
   }

   @PostMapping("/merchant")
   public ResponseEntity<User> createMerchantUser(@RequestBody @Valid UserDTO userDTO) throws Exception {
      User newUser = merchantUserService.createUser(userDTO);
      return ResponseEntity.ok(newUser);
   }

   @GetMapping
   public ResponseEntity<List<User>> getAllUsers(){
      List<User> users = this.repository.findAll();
      return ResponseEntity.ok(users);
   }
}
