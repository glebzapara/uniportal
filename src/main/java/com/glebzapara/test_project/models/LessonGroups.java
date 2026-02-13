package com.glebzapara.test_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "lesson_groups",
    uniqueConstraints = @UniqueConstraint(columnNames = {"lesson_id", "group_id"})
)
@Data
@NoArgsConstructor
public class LessonGroups {

    @EmbeddedId
    private LessonGroupId id;

    @NotNull
    @ManyToOne
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @NotNull
    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
