package com.financenavigator.controller;

import com.financenavigator.dto.UserDTO;
import com.financenavigator.entity.Users;
import com.financenavigator.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Slf4j
public class AuthController {

//    @Autowired
//    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        Users user = userService.login(userDTO.getUsername(), userDTO.getPassword());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

//        String token = jwtUtils.generateJwtToken(user);
        String token = generateRandomCode();
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        log.info("Signup for : "+userDTO.getUsername());
        Users existingUser = userService.findUserByUsername(userDTO.getUsername());
        if(userDTO.getUsername() == null || userDTO.getPassword() == null || StringUtils.isEmpty(userDTO.getUsername()) || StringUtils.isEmpty(userDTO.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or Password cannot be null");
        }
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        Users user = userService.createUser(userDTO);
//        String token = jwtUtils.generateJwtToken(user);
        String token = generateRandomCode();
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    public String generateRandomCode(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
