package com.example.ItauCuboNet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, columnDefinition = "varchar(30)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "varchar(50)")
    private String lastName;

    @Column(name = "enterprise", nullable = false, columnDefinition = "varchar(80)", unique = true)
    private String enterprise;

    @Column(name="participation", nullable = false, columnDefinition = "float")
    private Float participation;

}
