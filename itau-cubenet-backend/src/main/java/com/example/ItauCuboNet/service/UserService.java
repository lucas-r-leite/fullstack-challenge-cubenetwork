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
        if (user == null || user.getFirstName() == null || user.getLastName() == null || user.getEnterprise() == null || user.getParticipation() == null) {
            throw new RuntimeException("Invalid user data");
        }
        float totalParticipation = getTotalParticipationByEnterprise(user.getEnterprise()) + user.getParticipation();
        if (totalParticipation > 100) {
            throw new Exception("Total participation for enterprise " + user.getEnterprise() + " cannot exceed 100. Current total: " + getTotalParticipationByEnterprise(user.getEnterprise()));
        }
        return userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEnterprise(), user.getParticipation());
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public User updateUser(User updatedUser) throws Exception {
        if (updatedUser == null || updatedUser.getFirstName() == null || updatedUser.getLastName() == null) {
            throw new RuntimeException("Invalid user data");
        }

        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new Exception("User not found"));

        float totalParticipation = getTotalParticipationByEnterprise(updatedUser.getEnterprise()) - existingUser.getParticipation() + updatedUser.getParticipation();
        if (totalParticipation > 100) {
            throw new Exception("Total participation for enterprise " + updatedUser.getEnterprise() + " cannot exceed 100. Current total: " + getTotalParticipationByEnterprise(updatedUser.getEnterprise()));
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEnterprise(updatedUser.getEnterprise());
        existingUser.setParticipation(updatedUser.getParticipation());

        return userRepository.save(existingUser);
    }

    private float getTotalParticipationByEnterprise(String enterprise) {
        return userRepository.findByEnterprise(enterprise).stream()
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
