package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.models.Lesson;
import com.glebzapara.uniportal.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByGroupId(Integer groupId);
//    List<Lesson> findByTeacherAndGroupId(Teacher teacher, Integer groupId);
    List<Lesson> findByTeacherId(Integer teacherId);
    List<Lesson> findByTeacherIdAndGroupId(Integer teacherId, Integer groupId);
    List<Lesson> findBySubjectId(Integer subjectId);
//    Optional<Lesson> findFirstByTeacherAndGroupId(Teacher teacher, Integer groupId);
    boolean existsByTeacherIdAndSubjectId(Integer teacherId, Integer subjectId);

    @Query("SELECT DISTINCT l.group FROM Lesson l WHERE l.teacher.id = :teacherId")
    List<Group> findGroupsByTeacherId(Integer teacherId);
}
