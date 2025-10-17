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
    private String name;

    @NotNull
    @Min(1)
    @Max(6)
    private Short course;

    @NotNull
    @Min(1)
    @Max(8)
    private Integer faculty;

    @NotNull
    @Pattern(regexp = "[A-Z]+[1-8]")
    private String speciality;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
