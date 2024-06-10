package com.example.ItauCuboNet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ItauCuboNet.dto.UserDTO;
import com.example.ItauCuboNet.entity.User;
import com.example.ItauCuboNet.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getParticipation());
    }


    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                              .map(this::convertToDTO)
                              .toList();
    }

    public User updateUser(User updatedUser) {
        return userRepository.findByName(updatedUser.getName())
                             .map(existingUser -> {
                                 existingUser.setName(updatedUser.getName());
                                 existingUser.setParticipation(updatedUser.getParticipation());
                                 return userRepository.save(existingUser);
                             })
                             .orElse(null);
    }
}
