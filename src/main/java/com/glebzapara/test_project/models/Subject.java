package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(1)
    @Max(6)
    @Column(name = "course", nullable = false)
    private Short course;

    @NotNull
    @Column(name = "faculty", nullable = false)
    private Integer faculty;

    @NotNull
    @Size(max = 2)
    @Column(name = "speciality", nullable = false, length = 2)
    private String speciality;
}