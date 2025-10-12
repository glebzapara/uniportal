package com.glebzapara.test_project.controllers;

import com.glebzapara.test_project.models.Admin;
import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.models.Subject;
import com.glebzapara.test_project.models.Teacher;
import com.glebzapara.test_project.services.AdminService;
import com.glebzapara.test_project.services.StudentService;
import com.glebzapara.test_project.services.SubjectService;
import com.glebzapara.test_project.services.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller()
public class UniversityController {
    AdminService adminService;
    StudentService studentService;
    TeacherService teacherService;
    SubjectService subjectService;

    public UniversityController(AdminService adminService,
                                StudentService studentService,
                                TeacherService teacherService,
                                SubjectService subjectService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterChoice() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/role-select")
    public String selectRole(@RequestParam("role") String role, HttpSession httpSession) {
        httpSession.setAttribute("role", role);
        if ("ADMIN".equals(role)) {
            return "redirect:/admins/new";
        } else if ("STUDENT".equals(role)) {
            return "redirect:/students/new";
        } else if ("TEACHER".equals(role)) {
            return "redirect:/teachers/new";
        }

        return "redirect:/register";
    }

    @GetMapping("/admins/{id}/name")
    public String getAdminName(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("adminName", adminService.getAdminNameById(id));

        return "adminName";
    }

    @GetMapping("/admins/{id}/surname")
    public String getAdminSurname(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("adminSurname", adminService.getAdminSurnameById(id));

        return "adminSurname";
    }

    @GetMapping("/admins/{id}/email")
    public String getAdminEmail(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("adminEmail", adminService.getAdminEmailById(id));

        return "adminEmail";
    }

    @GetMapping("/admins/{id}/password")
    public String getAdminPassword(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("adminPassword", adminService.getAdminPasswordById(id));

        return "adminPassword";
    }

    @GetMapping("/admins/{id}/country")
    public String getAdminCountry(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("adminCountry", adminService.getAdminCountryById(id));

        return "adminCountry";
    }

    @GetMapping("/admins/{id}/phone-number")
    public String getAdminPhoneNumber(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("adminPhoneNumber", adminService.getAdminPhoneNumberById(id));

        return "adminPhoneNumber";
    }

    @GetMapping("/admins/{id}/image")
    public String getAdminImage(@PathVariable Integer id, Model model) throws IOException {
        byte[] imageBytes = adminService.getAdminImageById(id);
        if (imageBytes != null && imageBytes.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            model.addAttribute("adminImage", "data:image/png;base64," + base64Image);
        } else {
            model.addAttribute("adminImage", null);
        }
        return "adminImage";
    }

    @GetMapping("/admins/new")
    public String newAdmin(Model model) {
        model.addAttribute("adminForm", new Admin());

        return "newAdmin";
    }

    @PostMapping("/admins")
    public String createAdmin(@ModelAttribute("adminForm") Admin admin,
                                @RequestParam("file") MultipartFile file,
                                HttpSession httpSession) throws Exception {
        String role = (String) httpSession.getAttribute("role");
        admin.setRole(role);

        adminService.registerAdmin(admin);

        if (file != null && !file.isEmpty()) {
            adminService.saveAdminImage(admin.getId(), file);
        }


        return "redirect:/admins/" + admin.getId() + "/profile";
    }

    @GetMapping("/admins/{id}/profile")
    public String getAdminProfile(@PathVariable Integer id, Model model) throws Exception {
        getAdminName(id, model);
        getAdminSurname(id, model);
        getAdminEmail(id, model);
        getAdminCountry(id, model);
        getAdminPhoneNumber(id, model);
        getAdminImage(id, model);

        return "adminProfile";
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
        model.addAttribute("studentCourse", studentService.getStudentCourseById(id));

        return "studentCourse";
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
        byte[] imageBytes = studentService.getStudentImageById(id);
        if (imageBytes != null && imageBytes.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            model.addAttribute("studentImage", "data:image/png;base64," + base64Image);
        } else {
            model.addAttribute("studentImage", null);
        }

        return "studentImage";
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("studentForm", new Student());

        return "newStudent";
    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute("studentForm") Student student,
                                @RequestParam("file") MultipartFile file,
                                HttpSession httpSession) throws Exception {
        String role = (String) httpSession.getAttribute("role");
        student.setRole(role);

        studentService.registerStudent(student);

        if (file != null && !file.isEmpty()) {
            studentService.saveStudentImage(student.getId(), file);
        }

        return "redirect:/students/" + student.getId() + "/profile";
    }

    @GetMapping("/students/{id}/profile")
    public String getStudentProfile(@PathVariable Integer id, Model model) throws Exception {
        getStudentName(id, model);
        getStudentSurname(id, model);
        getStudentEmail(id, model);
        getStudentFaculty(id, model);
        getStudentCourse(id, model);
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

    @GetMapping("/subjects/new")
    public String newSubject(Model model) {
        model.addAttribute("subjectForm", new Subject());
        return "newSubject";
    }

    @PostMapping("/subjects")
    public String createSubject(@ModelAttribute("subjectForm") Subject subject) throws Exception {
        subjectService.registerSubject(subject);

        return "redirect:";
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
        byte[] imageBytes = teacherService.getTeacherImageById(id);
        if (imageBytes != null && imageBytes.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            model.addAttribute("teacherImage", "data:image/png;base64," + base64Image);
        } else {
            model.addAttribute("teacherImage", null);
        }

        return "teacherImage";
    }

    @GetMapping("/teachers/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacherForm", new Teacher());
        return "newTeacher";
    }

    @PostMapping("/teachers")
    public String createTeacher(@ModelAttribute("teacherForm") Teacher teacher,
                                @RequestParam("file") MultipartFile file, HttpSession httpSession) throws Exception {
        String role = (String) httpSession.getAttribute("role");
        teacher.setRole(role);

        teacherService.registerTeacher(teacher);

        if (file != null && !file.isEmpty()) {
            teacherService.saveTeacherImage(teacher.getId(), file);
        }

        return "redirect:/teachers/" + teacher.getId() + "/profile";
    }

    @GetMapping("/teachers/{id}/profile")
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
