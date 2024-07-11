package br.com.picpay.controller;

import br.com.picpay.domain.user.MerchantUser;
import br.com.picpay.dto.DadosListagemUser;
import br.com.picpay.dto.UserDTO;
import br.com.picpay.services.CommonUserService;
import br.com.picpay.services.MerchantUserService;
import br.com.picpay.services.UserService;
import br.com.picpay.services.UserServiceFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class UserController {

   @Autowired
   private CommonUserService commonUserService;
   @Autowired
   private MerchantUserService merchantUserService;
   @Autowired
   private UserServiceFactory userServiceFactory;

   @PostMapping("/createCommonUser")
   public ResponseEntity<?> createCommonUser(@RequestBody @Valid UserDTO userDTO) throws Exception {
      try {
         return ResponseEntity.ok(commonUserService.createUser(userDTO));
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @PostMapping("/createMerchantUser")
   public ResponseEntity<?> createMerchantUser(@RequestBody @Valid UserDTO userDTO) throws Exception {
      try {
         return ResponseEntity.ok(merchantUserService.createUser(userDTO));
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping("/users")
   public ResponseEntity<?> getAllUsers(Pageable pageable){
      try {
         UserService<MerchantUser> userService = merchantUserService;
         Page<DadosListagemUser> users = userService.getAllUsers(pageable);
         return ResponseEntity.ok(users);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping("/TransactionsForUser/{id}")
   public ResponseEntity<?> getAllTransactionsForUser(@PathVariable Long id){
      try {
         return ResponseEntity.ok(userServiceFactory.getUserService(id).getTransactionsforUser(id));
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping("/user/{id}")
   public ResponseEntity<?> getUser(@PathVariable Long id){
      try {
         return ResponseEntity.ok(userServiceFactory.getUserService(id).findUserById(id));
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }


}
