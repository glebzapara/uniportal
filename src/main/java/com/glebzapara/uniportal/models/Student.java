package com.glebzapara.uniportal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@ToString(exclude = "password")
public class Student {

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
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 8, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @NotNull
    @Size(max = 16)
    @Pattern(regexp = "^\\+[0-9]{9,15}$")
    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "image")
    private String image;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @NotNull
    @Size(max = 20)
    @Column(name = "role", nullable = false, length = 20)
    private String role;
}