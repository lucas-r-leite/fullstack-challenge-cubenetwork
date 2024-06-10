package com.example.ItauCuboNet.service;

import java.util.List;
import java.util.Optional;

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
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getParticipation());
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

   public User userUpdate(User updatedUser) {
    Optional<User> existingUser = userRepository.findByName(updatedUser.getName());
    if (existingUser.isPresent()) {
        User userToUpdate = existingUser.get();
        if (updatedUser.getName()!= null) {
            userToUpdate.setName(updatedUser.getName());
        }
        if (updatedUser.getParticipation()!= null) {
            userToUpdate.setParticipation(updatedUser.getParticipation());
        }
        return userRepository.save(userToUpdate);
    } else {
        return null; // or throw an exception if you prefer
    }
} 

}
