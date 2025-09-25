package com.glebzapara.test_project.controllers;

import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.models.Teacher;
import com.glebzapara.test_project.services.StudentService;
import com.glebzapara.test_project.services.SubjectService;
import com.glebzapara.test_project.services.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller()
public class UniversityController {
    StudentService studentService;
    TeacherService teacherService;
    SubjectService subjectService;

    public UniversityController(StudentService studentService,
                                TeacherService teacherService,
                                SubjectService subjectService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/students/{id}/name")
    public String getStudentName(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentName", studentService.getStudentNameById(id));

        return "studentName";
    }

    @GetMapping("/students/{id}/surname")
    public String getStudentSurname(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentSurname", studentService.getStudentSurnameById(id));

        return "studentSurname";
    }

    @GetMapping("/students/{id}/email")
    public String getStudentEmail(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentEmail", studentService.getStudentEmailById(id));

        return "studentEmail";
    }

    @GetMapping("/students/{id}/password")
    public String getStudentPassword(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentPassword", studentService.getStudentPasswordById(id));

        return "studentPassword";
    }

    @GetMapping("/students/{id}/faculty")
    public String getStudentFaculty(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentFaculty", studentService.getStudentFacultyById(id));

        return "studentFaculty";
    }

    @GetMapping("/students/{id}/course")
    public String getStudentCourse(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentsCourse", studentService.getStudentCourseById(id));

        return "studentsCourse";
    }

    @GetMapping("/students/{id}/speciality")
    public String getStudentSpeciality(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentSpeciality", studentService.getStudentSpecialityById(id));

        return "studentSpeciality";
    }

    @GetMapping("/students/{id}/country")
    public String getStudentCountry(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentCountry", studentService.getStudentCountryById(id));

        return "studentCountry";
    }

    @GetMapping("/students/{id}/phone-number")
    public String getStudentPhoneNumber(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentPhoneNumber", studentService.getStudentPhoneNumberById(id));

        return "studentPhoneNumber";
    }

    @GetMapping("/students/{id}/image")
    public String getStudentImage(@PathVariable Integer id, Model model) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(studentService.getStudentImageById(id));
        model.addAttribute("studentImage", "data:image/png;base64," + base64Image);

        return "studentImage";
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("studentForm", new Student());
        return "newStudent";
    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute("studentForm") Student student,
                                @RequestParam("file") MultipartFile file) throws Exception {
        studentService.registerStudent(student);

        if (file != null && !file.isEmpty()) {
            studentService.saveStudentImage(student.getId(), file);
        }

        return "redirect:";
    }

    @GetMapping("students/{id}/profile")
    public String getStudentProfile(@PathVariable Integer id, Model model) throws Exception {
        getStudentName(id, model);
        getStudentSurname(id, model);
        getStudentEmail(id, model);
        getStudentFaculty(id, model);
        getStudentSpeciality(id, model);
        getStudentCountry(id, model);
        getStudentPhoneNumber(id, model);
        getStudentImage(id, model);

        return "studentProfile";
    }

    @GetMapping("/subjects/{id}/name")
    public String getSubjectName(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectName", subjectService.getSubjectNameById(id));

        return "subjectName";
    }

    @GetMapping("/subjects/{id}/faculty")
    public String getSubjectFaculty(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectFaculty", subjectService.getSubjectFacultyById(id));

        return "subjectFaculty";
    }

    @GetMapping("/subjects/{id}/course")
    public String getSubjectCourse(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectCourse", subjectService.getSubjectCourseById(id));

        return "subjectCourse";
    }

    @GetMapping("/subjects/{id}/speciality")
    public String getSubjectSpeciality(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectSpeciality", subjectService.getSubjectSpecialityById(id));

        return "subjectSpeciality";
    }

    @GetMapping("/teachers/{id}/name")
    public String getTeacherName(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("teacherName", teacherService.getTeacherNameById(id));

        return "teacherName";
    }

    @GetMapping("/teachers/{id}/surname")
    public String getTeacherSurname(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("teacherSurname", teacherService.getTeacherSurnameById(id));

        return "teacherSurname";
    }

    @GetMapping("/teachers/{id}/email")
    public String getTeacherEmail(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("teacherEmail", teacherService.getTeacherEmailById(id));

        return "teacherEmail";
    }

    @GetMapping("/teachers/{id}/password")
    public String getTeacherPassword(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("teacherPassword", teacherService.getTeacherPasswordById(id));

        return "teacherPassword";
    }

    @GetMapping("/teachers/{id}/country")
    public String getTeacherCountry(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("teacherCountry", teacherService.getTeacherCountryById(id));

        return "teacherCountry";
    }

    @GetMapping("/teachers/{id}/phone-number")
    public String getTeacherPhoneNumber(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("teacherPhoneNumber", teacherService.getTeacherPhoneNumberById(id));

        return "teacherPhoneNumber";
    }

    @GetMapping("/teachers/{id}/image")
    public String getTeacherImage(@PathVariable Integer id, Model model) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(teacherService.getTeacherImageById(id));
        model.addAttribute("teacherImage", "data:image/png;base64," + base64Image);

        return "teacherImage";
    }

    @GetMapping("/teachers/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacherForm", new Teacher());
        return "newTeacher";
    }

    @PostMapping("/teachers")
    public String createTeacher(@ModelAttribute("teacherForm") Teacher teacher,
                                @RequestParam("file") MultipartFile file) throws Exception {
        teacherService.registerTeacher(teacher);

        if (file != null && !file.isEmpty()) {
            teacherService.saveTeacherImage(teacher.getId(), file);
        }

        return "redirect:";
    }

    @GetMapping("teachers/{id}/profile")
    public String getTeacherProfile(@PathVariable Integer id, Model model) throws Exception {
        getTeacherName(id, model);
        getTeacherSurname(id, model);
        getTeacherEmail(id, model);
        getTeacherCountry(id, model);
        getTeacherPhoneNumber(id, model);
        getTeacherImage(id, model);

        return "teacherProfile";
    }
}
