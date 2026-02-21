package com.glebzapara.test_project.controllers.mvc;

import com.glebzapara.test_project.models.*;
import com.glebzapara.test_project.security.AdminDetails;
import com.glebzapara.test_project.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    GroupService groupService;
    DepartmentService departmentService;
    SubjectService subjectService;

    public UniversityController(AdminService adminService,
                                StudentService studentService,
                                TeacherService teacherService,
                                GroupService groupService,
                                DepartmentService departmentService,
                                SubjectService subjectService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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

    @GetMapping("/admins/new")
    public String newAdmin(Model model) {
        model.addAttribute("adminForm", new Admin());

        return "newAdmin";
    }

    @PostMapping("/admins")
    public String createAdmin(@ModelAttribute("adminForm") Admin admin) throws Exception {

        admin.setRole("ROLE_ADMIN");

        adminService.registerAdmin(admin);

        return "redirect:/login";
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

//    private String getStudentGroupName(@PathVariable Integer id, Model model) throws Exception {
//        model.addAttribute("studentGroupName", studentService.getStudentGroupNameById(id));
//
//        return "studentGroupName";
//    }

//    @GetMapping("/students/{id}/faculty")
//    public String getStudentGroupFaculty(@PathVariable Integer id, Model model) throws Exception {
//        model.addAttribute("studentGroupFaculty", studentService.getStudentGroupFacultyById(id));
//
//        return "studentGroupFaculty";
//    }

    @GetMapping("/students/{id}/course")
    public String getStudentGroupCourse(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentGroupCourse", studentService.getStudentGroupCourseById(id));

        return "studentGroupCourse";
    }

    @GetMapping("/students/{id}/speciality")
    public String getStudentGroupSpeciality(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentGroupSpeciality", studentService.getStudentGroupSpecialityById(id));

        return "studentGroupSpeciality";
    }

    @GetMapping("/students/{id}/phone-number")
    public String getStudentPhoneNumber(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("studentPhoneNumber", studentService.getStudentPhoneNumberById(id));

        return "studentPhoneNumber";
    }

    @GetMapping("/students/{id}/image")
    public String getStudentImage(@PathVariable Integer id, Model model) {

        String imageUrl = studentService.getStudentImageById(id);
        model.addAttribute("studentImage", imageUrl);

        return "studentImage";
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("studentForm", new Student());
        model.addAttribute("groups", groupService.findAll());

        return "newStudent";
    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute("studentForm") Student student,
                                @RequestParam("groupId") Integer groupId,
                                @RequestParam("file") MultipartFile file,
                                Authentication authentication) throws Exception {

        student.setRole("ROLE_STUDENT");

        studentService.registerStudent(student, groupId);

        if (file != null && !file.isEmpty()) {
            studentService.saveStudentImage(student.getId(), file);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AdminDetails) {
            return "redirect:/";
        } else {
            return "redirect:/students/" + student.getId() + "/profile";
        }
    }

    @GetMapping("/students/{id}/profile")
    public String getStudentProfile(@PathVariable Integer id, Model model) throws Exception {
        getStudentName(id, model);
        getStudentSurname(id, model);
        getStudentEmail(id, model);
//        getStudentGroupName(id, model);
//        getStudentGroupFaculty(id, model);
//        getStudentGroupCourse(id, model);
//        getStudentGroupSpeciality(id, model);
        getStudentPhoneNumber(id, model);
        getStudentImage(id, model);

        return "studentProfile";
    }

    @GetMapping("/subjects/{id}/name")
    public String getSubjectName(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectName", subjectService.getSubjectNameById(id));

        return "subjectName";
    }

//    @GetMapping("/subjects/{id}/faculty")
//    public String getSubjectFaculty(@PathVariable Integer id, Model model) throws Exception {
//        model.addAttribute("subjectFaculty", subjectService.getSubjectFacultyById(id));
//
//        return "subjectFaculty";
//    }

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

//    @GetMapping("/teachers/{id}/password")
//    public String getTeacherPassword(@PathVariable Integer id, Model model) throws Exception {
//        model.addAttribute("teacherPassword", teacherService.getTeacherPasswordById(id));
//
//        return "teacherPassword";
//    }

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
        model.addAttribute("departments", departmentService.findAll());
        return "newTeacher";
    }

    @PostMapping("/teachers")
    public String createTeacher(@ModelAttribute("teacherForm") Teacher teacher,
                                @RequestParam("departmentId") Integer departmentId,
                                @RequestParam("file") MultipartFile file) throws Exception {

        teacher.setRole("ROLE_TEACHER");

        teacherService.registerTeacher(teacher, departmentId);

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
        getTeacherPhoneNumber(id, model);
        getTeacherImage(id, model);

        return "teacherProfile";
    }

    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handle(Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
