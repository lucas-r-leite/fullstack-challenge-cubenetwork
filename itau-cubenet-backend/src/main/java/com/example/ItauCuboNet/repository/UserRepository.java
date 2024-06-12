package com.example.ItauCuboNet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ItauCuboNet.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
    List<User> findByEnterprise(String enterprise);
}