package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.*;
import com.glebzapara.uniportal.models.enums.DayOfWeek;
import com.glebzapara.uniportal.repositories.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LessonService {

    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;

    public LessonService(StudentRepository studentRepository,
                         LessonRepository lessonRepository,
                         SubjectRepository subjectRepository,
                         SubjectService subjectService,
                         GroupService groupService,
                         TeacherService teacherService,
                         TeacherRepository teacherRepository,
                         GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
    }

    public Map<DayOfWeek, List<Lesson>> getScheduleForStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow();

        List<Lesson> lessons = lessonRepository.findByGroupId(student.getGroup().getId());

        Map<DayOfWeek, List<Lesson>> schedule = new HashMap<>();

        for (Lesson lesson : lessons) {
            schedule
                    .computeIfAbsent(lesson.getDayOfWeek(), k -> new ArrayList<>())
                    .add(lesson);
        }

        return schedule;
    }

    public List<Lesson> findByTeacherAndGroup(Teacher teacher, Integer groupId) {
        return lessonRepository.findByTeacherAndGroupId(teacher, groupId);
    }

    public List<Group> findGroupsByTeacher(Teacher teacher) {
        return lessonRepository.findGroupsByTeacher(teacher);
    }

    public Lesson findLessonByTime(List<Lesson> lessons, String time) {
        for (Lesson l : lessons) {
            String lessonTime = l.getStartTime() + " - " + l.getEndTime();
            if (lessonTime.equals(time)) {
                return l;
            }
        }

        return null;
    }

    public Map<DayOfWeek, List<Lesson>> getScheduleForTeacher(Integer teacherId) {

        List<Lesson> lessons = lessonRepository.findByTeacherId(teacherId);

        Map<DayOfWeek, List<Lesson>> schedule = new HashMap<>();

        for (Lesson lesson : lessons) {
            schedule
                    .computeIfAbsent(lesson.getDayOfWeek(), k -> new ArrayList<>())
                    .add(lesson);
        }

        return schedule;
    }

    public Map<DayOfWeek, List<Lesson>> getScheduleForTeacherByGroup(Integer teacherId, Integer groupId) {
        List<Lesson> lessons = lessonRepository.findByTeacherIdAndGroupId(teacherId, groupId);

        Map<DayOfWeek, List<Lesson>> schedule = new HashMap<>();

        for (Lesson lesson : lessons) {
            schedule
                    .computeIfAbsent(lesson.getDayOfWeek(), k -> new ArrayList<>())
                    .add(lesson);
        }

        return schedule;
    }

    public void createLesson(Lesson lesson,
                             Integer subjectId,
                             Integer teacherId,
                             Integer groupId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        lesson.setSubject(subject);
        lesson.setTeacher(teacher);
        lesson.setGroup(group);

        lessonRepository.save(lesson);
    }
}
