package com.glebzapara.uniportal.controllers.mvc;

import com.glebzapara.uniportal.models.*;
import com.glebzapara.uniportal.security.AdminDetails;
import com.glebzapara.uniportal.security.StudentDetails;
import com.glebzapara.uniportal.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        Student student = studentService.findOneStudent(id)
                .orElseThrow(() -> new Exception("Student not found"));

        model.addAttribute("student", student);

        return "studentProfile";
    }

    @GetMapping("/subjects/{id}/name")
    public String getSubjectName(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectName", subjectService.getSubjectNameById(id));

        return "subjectName";
    }

    @GetMapping("/subjects/{id}/course")
    public String getSubjectCourse(@PathVariable Integer id, Model model) throws Exception {
        model.addAttribute("subjectCourse", subjectService.getSubjectCourseById(id));

        return "subjectCourse";
    }

    @GetMapping("/subjects/new")
    public String newSubject(Model model) {
        model.addAttribute("subjectForm", new Subject());

        return "newSubject";
    }

    @PostMapping("/subjects")
    public String createSubject(@ModelAttribute("subjectForm") Subject subject) throws Exception {
        subjectService.registerSubject(subject);

        return "redirect:/";
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
    public String getTeachersProfile(@PathVariable Integer id, Model model) throws Exception {
        Teacher teacher = teacherService.findOneTeacher(id)
                .orElseThrow(() -> new Exception("Teacher not found"));

        model.addAttribute( "teacher", teacher);

        return "teacherProfile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @GetMapping("/schedule")
    public String schedule() {
        return "schedule";
    }

    @GetMapping("/my-group")
    public String myGroup(Authentication authentication, Model model) throws Exception {
        Object principal = authentication.getPrincipal();

        if (principal instanceof StudentDetails studentDetails) {
            Student student = studentDetails.getStudent();
            Group group = student.getGroup();

            model.addAttribute("group", student.getGroup());
            model.addAttribute("students", studentService.findByGroup(group));
        }

        return "myGroup";
    }

    @GetMapping("/teachers")
    public String teachers() {
        return "teachers";
    }

    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handle(Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
