package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    protected static final Logger log = LogManager.getLogger(UserController.class.getName());


    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.debug("creating user");
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        log.debug("cart created with id = " + cart.getId());
        user.setCart(cart);
        String hashed;

        String pass = createUserRequest.getPassword();
        String cPass = createUserRequest.getConfirmPassword();

        if (pass.length() < 4 || cPass.length() < 4){
            log.error("password too short, should be at least 4 chars");
            throw new UnsupportedOperationException("password too short");
        }

        if (Objects.equals(createUserRequest.getPassword(), createUserRequest.getConfirmPassword())) {
            hashed = bCryptPasswordEncoder.encode(createUserRequest.getPassword());
            log.debug("hashed password generated");
        } else {
            log.error("input password and confirmation password don't match");
            throw new UnsupportedOperationException("password don't match");
        }

        user.setPassword(hashed);
        userRepository.save(user);
        log.debug("user created with userId = " + user.getId());
        return ResponseEntity.ok(user);
    }
}
