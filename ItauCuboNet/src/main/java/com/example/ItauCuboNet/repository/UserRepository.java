package com.example.ItauCuboNet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ItauCuboNet.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional <User> findByName(String name);

}
