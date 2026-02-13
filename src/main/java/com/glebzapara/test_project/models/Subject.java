package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Size(max = 2)
    @Pattern(regexp = "[A-Z]+[1-8]")
    @Column(name = "speciality")
    private String speciality;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
