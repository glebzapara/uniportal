package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Lesson;
import com.glebzapara.uniportal.models.Student;
import com.glebzapara.uniportal.models.enums.DayOfWeek;
import com.glebzapara.uniportal.repositories.LessonRepository;
import com.glebzapara.uniportal.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LessonService {

    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    public LessonService(StudentRepository studentRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
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

    public Lesson findLessonByTime(List<Lesson> lessons, String time) {
        for (Lesson l : lessons) {
            String lessonTime = l.getStartTime() + " - " + l.getEndTime();
            if (lessonTime.equals(time)) {
                return l;
            }
        }

        return null;
    }
}
