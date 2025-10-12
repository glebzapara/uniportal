package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 8, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Pattern(regexp = "^[A-Z]{2,3}$")
    @Column(name = "country", nullable = false, length = 3)
    private String country;

    @NotNull
    @Pattern(regexp = "^\\+[0-9]{9,15}$")
    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "image")
    private String image;

    @NotNull
    @Size(max = 10)
    @Column(name = "role", nullable = false, length = 10)
    private String role;
}