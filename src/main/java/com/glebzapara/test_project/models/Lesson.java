package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "students_id", nullable = false)
    private Student student;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subjects_id", nullable = false)
    private Subject subject;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "teachers_id", nullable = false)
    private Teacher teacher;

    @NotNull
    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Min(0)
    @Max(3)
    @Column(name = "score")
    private Short score;
}