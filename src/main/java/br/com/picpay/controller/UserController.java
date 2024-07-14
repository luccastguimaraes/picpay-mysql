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
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

   @Autowired
   private CommonUserService commonUserService;
   @Autowired
   private MerchantUserService merchantUserService;
   @Autowired
   private UserServiceFactory userServiceFactory;

   @PostMapping("/createCommonUser")
   public ResponseEntity<?> createCommonUser(@RequestBody @Valid UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) throws Exception {
      try {
         DadosListagemUser user = commonUserService.createUser(userDTO);
         var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.id()).toUri();
         return ResponseEntity.created(uri).body(user);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @PostMapping("/createMerchantUser")
   public ResponseEntity<?> createMerchantUser(@RequestBody @Valid UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) throws Exception {
      try {
         DadosListagemUser user = merchantUserService.createUser(userDTO);
         var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.id()).toUri();
         return ResponseEntity.created(uri).body(user);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping
   public ResponseEntity<?> getAllUsers(Pageable pageable){
      try {
         UserService<MerchantUser> userService = merchantUserService;
         Page<DadosListagemUser> users = userService.getAllUsers(pageable);
         return ResponseEntity.ok().body(users);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping("/{id}")
   public ResponseEntity<?> getUser(@PathVariable Long id){
      try {
         DadosListagemUser userById = userServiceFactory.getUserService(id).getUser(id);
         return ResponseEntity.ok().body(userById);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

}
