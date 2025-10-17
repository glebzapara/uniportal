package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "lesson_groups",
    uniqueConstraints = @UniqueConstraint(columnNames = {"lesson_id", "group_id"})
)
@Data
@NoArgsConstructor
public class LessonGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
