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
    public User save(User user) throws Exception {
        float totalParticipation = getTotalParticipation() + user.getParticipation();
        if (totalParticipation > 100) {
            throw new Exception("Total participation cannot exceed 100. Current total: " + getTotalParticipation());
        }
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

    public User updateUser(User updatedUser) throws Exception {
        User existingUser = userRepository.findByName(updatedUser.getName())
                                          .orElseThrow(() -> new Exception("User not found"));

        float totalParticipation = getTotalParticipation() - existingUser.getParticipation() + updatedUser.getParticipation();
        if (totalParticipation > 100) {
            throw new Exception("Total participation cannot exceed 100. Current total: " + getTotalParticipation());
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setParticipation(updatedUser.getParticipation());

        return userRepository.save(existingUser);
    }

    private float getTotalParticipation() {
        return userRepository.findAll().stream()
                              .map(User::getParticipation)
                              .reduce(0f, Float::sum);
    }
    public void deleteUser(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new Exception("User not found");
        }
    }
}