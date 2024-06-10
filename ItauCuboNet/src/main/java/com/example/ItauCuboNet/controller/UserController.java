package com.example.ItauCuboNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ItauCuboNet.dto.UserDTO;
import com.example.ItauCuboNet.entity.User;
import com.example.ItauCuboNet.repository.UserRepository;
import com.example.ItauCuboNet.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<?> createUser(@RequestBody User body) {
        if (userRepository.findByName(body.getName()).isPresent()) {
            return ResponseEntity.status(400).body("User with the same name already exists");
        }

        User savedUser = userService.save(body);
        return new ResponseEntity<>(savedUser, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        User updated = userService.updateUser(updatedUser);
        if (updated != null) {
            return new ResponseEntity<>(updated, org.springframework.http.HttpStatus.OK);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}