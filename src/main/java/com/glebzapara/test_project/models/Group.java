package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(1)
    @Max(8)
    private Integer faculty;

    @NotNull
    @Min(1)
    @Max(6)
    private Short course;

    @NotNull
    @Pattern(regexp = "[A-Z]+[1-8]")
    private String speciality;

    @Column(name = "number")
    private Integer number;

    @Formula("faculty || course || number")
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}

