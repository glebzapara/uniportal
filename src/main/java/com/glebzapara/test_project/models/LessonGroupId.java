package com.glebzapara.test_project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LessonGroupId implements Serializable {

    @Column(name = "lesson_id")
    private Integer lessonId;

    @Column(name = "group_id")
    private Integer groupId;

    public LessonGroupId() {
    }

    public LessonGroupId(Integer lessonId, Integer groupId) {
        this.lessonId = lessonId;
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LessonGroupId that = (LessonGroupId) o;
        return Objects.equals(lessonId, that.lessonId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, groupId);
    }
}
