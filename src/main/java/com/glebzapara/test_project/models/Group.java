package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Min(1)
    @Max(6)
    @Column(name = "course", nullable = false)
    private Short course;

    @NotNull
    @Pattern(regexp = "[A-Z]+[1-8]")
    @Column(name = "speciality", nullable = false)
    private String speciality;
}

