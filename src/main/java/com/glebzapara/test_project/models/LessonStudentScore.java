package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "lesson_student_scores",
    uniqueConstraints = @UniqueConstraint(columnNames = {"lesson_id", "student_id"})
)
@Data
@NoArgsConstructor
public class LessonStudentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @Min(0)
    @Max(3)
    private short score;
}
