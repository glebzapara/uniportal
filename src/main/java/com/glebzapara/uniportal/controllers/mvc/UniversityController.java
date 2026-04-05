package com.glebzapara.uniportal.controllers.mvc;

import com.glebzapara.uniportal.models.*;
import com.glebzapara.uniportal.models.enums.DayOfWeek;
import com.glebzapara.uniportal.security.StudentDetails;
import com.glebzapara.uniportal.security.TeacherDetails;
import com.glebzapara.uniportal.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@Controller()
public class UniversityController {

    AdminService adminService;
    StudentService studentService;
    TeacherService teacherService;
    GroupService groupService;
    DepartmentService departmentService;
    SubjectService subjectService;
    LessonService lessonService;
    GradeService gradeService;

    public UniversityController(AdminService adminService,
                                StudentService studentService,
                                TeacherService teacherService,
                                GroupService groupService,
                                DepartmentService departmentService,
                                SubjectService subjectService,
                                LessonService lessonService,
                                GradeService gradeService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
        this.lessonService = lessonService;
        this.gradeService = gradeService;
    }

    @GetMapping("/")
    public String homePage(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof StudentDetails studentDetails) {
            Student student = studentDetails.getStudent();

            model.addAttribute("subjects",
                    lessonService.findSubjectsByGroup(student.getGroup()
                    )
            );
        } else if (principal instanceof TeacherDetails teacherDetails) {
            Teacher teacher = teacherDetails.getTeacher();

            model.addAttribute("groups", lessonService.findGroupsByTeacher(teacher.getId()));

            return "teacher-dashboard";
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admins/new")
    public String showCreateAdminForm(Model model) {
        model.addAttribute("adminForm", new Admin());

        return "admin-form";
    }

    @PostMapping("/admins")
    public String createAdmin(@ModelAttribute("adminForm") Admin admin) throws Exception {
        admin.setRole("ROLE_ADMIN");
        adminService.registerAdmin(admin);

        return "redirect:/";
    }

    @GetMapping("/admins/edit/{id}")
    public String editAdmin(@PathVariable Integer id, Model model) {
        Admin admin = adminService.findById(id);

        model.addAttribute("adminForm", admin);

        return "admin-form";
    }

    @GetMapping("/admins/delete/{id}")
    public String deleteAdmin(@PathVariable Integer id) {
        adminService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        model.addAttribute("students", studentService.findAllStudents());

        return "students";
    }

    @GetMapping("/students/new")
    public String showCreateStudentForm(Model model) {
        model.addAttribute("studentForm", new Student());
        model.addAttribute("groups", groupService.findAllGroups());

        return "student-form";
    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute("studentForm") Student student,
                                @RequestParam("groupId") Integer groupId,
                                @RequestParam("file") MultipartFile file) throws Exception {
        student.setRole("ROLE_STUDENT");

        studentService.registerStudent(student, groupId);

        if (file != null && !file.isEmpty()) {
            studentService.saveStudentImage(student.getId(), file);
        }

        return "redirect:/students/" + student.getId() + "/profile";
    }

    @GetMapping("/students/{id}/profile")
    public String getStudentProfile(@PathVariable Integer id, Model model, Authentication authentication) {
        Student student = studentService.findOneStudent(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof StudentDetails studentDetails) {
                model.addAttribute("currentStudentId", studentDetails.getStudent().getId());
            }
        }

        model.addAttribute("student", student);

        return "student-profile";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudent(@PathVariable Integer id, Model model) {
        Student student = studentService.findById(id);

        model.addAttribute("studentForm", student);
        model.addAttribute("groups", groupService.findAllGroups());

        return "student-form";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/teachers")
    public String getTeachersPage(Model model) {
        List<Teacher> teachers = teacherService.findAllTeachers();

        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherSubjects",
                teacherService.getTeacherSubjects(teachers));

        return "teachers";
    }

    @GetMapping("/teachers/new")
    public String showCreateTeacherForm(Model model) {
        model.addAttribute("teacherForm", new Teacher());
        model.addAttribute("departments", departmentService.findAllDepartments());

        return "teacher-form";
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
    public String getTeacherProfile(@PathVariable Integer id, Model model, Authentication authentication) {
        List<Teacher> teachers = teacherService.findAllTeachers();
        Teacher teacher = teacherService.findOneTeacher(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof TeacherDetails teacherDetails) {
                model.addAttribute("currentTeacherId", teacherDetails.getTeacher().getId());
            }
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("teacherSubjects",
                teacherService.getTeacherSubjects(teachers));

        return "teacher-profile";
    }

    @GetMapping("/teachers/edit/{id}")
    public String editTeacher(@PathVariable Integer id, Model model) {
        Teacher teacher = teacherService.findById(id);

        model.addAttribute("teacherForm", teacher);
        model.addAttribute("departments", departmentService.findAllDepartments());

        return "teacher-form";
    }

    @GetMapping("/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable Integer id) {
        teacherService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/teacher/groups/{groupId}")
    public String getTeacherGroupSchedule(@PathVariable Integer groupId,
                                          Model model,
                                          Principal principal) {
        String email = principal.getName();

        Teacher teacher = teacherService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Map<DayOfWeek, List<Lesson>> schedule =
                lessonService.getScheduleForTeacherByGroup(teacher.getId(), groupId);

        for (DayOfWeek d : DayOfWeek.values()) {
            schedule.putIfAbsent(d, new ArrayList<>());
        }

        model.addAttribute("schedule", schedule);
        model.addAttribute("timeSlots", List.of(
                "08:00 - 09:35",
                "09:50 - 11:25",
                "11:55 - 13:30",
                "13:45 - 15:20"
        ));
        model.addAttribute("lessonService", lessonService);
        model.addAttribute("days", DayOfWeek.values());

        return "schedule";
    }

    @GetMapping("/group")
    public String getStudentGroup(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof StudentDetails studentDetails) {
            Student student = studentDetails.getStudent();
            Group group = student.getGroup();

            model.addAttribute("group", student.getGroup());
            model.addAttribute("students", studentService.findByGroup(group));

            return "group";
        }

        return "redirect:/";
    }

    @GetMapping("/groups/new")
    public String showCreateGroupForm(@ModelAttribute Group group,  Model model) {
        model.addAttribute("groupForm", group);
        model.addAttribute("departments", departmentService.findAllDepartments());

        return "group-form";
    }

    @PostMapping("/groups")
    public String createGroup(@ModelAttribute("groupForm") Group group,
                              @RequestParam("departmentId") Integer departmentId) {
        groupService.createGroup(group, departmentId);

        return "redirect:/";
    }

    @GetMapping("/groups/edit/{id}")
    public String editGroup(@PathVariable Integer id, Model model) {
        Group group = groupService.findById(id);

        model.addAttribute("groupForm", group);
        model.addAttribute("departments", departmentService.findAllDepartments());

        return "group-form";
    }

    @GetMapping("/groups/delete/{id}")
    public String deleteGroup(@PathVariable Integer id) {
        groupService.deleteById(id);

        return "redirect:/";
    }

    @PostMapping("/group/search")
    public String searchStudentsInGroup(@RequestParam("searchTerm") String searchTerm,
                                        Authentication authentication,
                                        Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof StudentDetails studentDetails) {
            Group group = studentDetails.getStudent().getGroup();
            List<Student> students = studentService.findByGroup(group);
            List<Student> filtered = new ArrayList<>();

            for (Student student : students) {
                String fullName = (student.getName() + " " + student.getSurname()).toLowerCase();

                if (fullName.contains(searchTerm.toLowerCase()) ||
                        student.getPhoneNumber().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filtered.add(student);
                }
            }

            model.addAttribute("group", group);
            model.addAttribute("students", filtered);

            return "group";
        }

        return "redirect:/";
    }

    @GetMapping("/departments/new")
    public String showCreateDepartmentForm(@ModelAttribute Department department,  Model model) {
        model.addAttribute("departmentForm", department);

        return "department-form";
    }

    @PostMapping("/departments")
    public String createDepartment(@ModelAttribute("departmentForm") Department department) {
        departmentService.createDepartment(department);

        return "redirect:/";
    }

    @GetMapping("/departments/edit/{id}")
    public String editDepartment(@PathVariable Integer id, Model model) {
        Department department = departmentService.findById(id);

        model.addAttribute("departmentForm", department);

        return "department-form";
    }

    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/subjects/new")
    public String showCreateSubjectForm(Model model) {
        model.addAttribute("subjectForm", new Subject());
        model.addAttribute("departments", departmentService.findAllDepartments());

        return "subject-form";
    }

    @PostMapping("/subjects")
    public String createSubject(@ModelAttribute("subjectForm") Subject subject,
                                @RequestParam("departmentId") Integer departmentId) {
        subjectService.registerSubject(subject, departmentId);

        return "redirect:/";
    }

    @GetMapping("/subjects/{id}")
    public String getSubjectPage(@PathVariable Integer id,
                                 Model model,
                                 Authentication authentication) {
        Subject subject = subjectService.findById(id);

        model.addAttribute("subject", subject);

        Teacher teacher = lessonService.findFirstTeacherBySubjectId(id);

        model.addAttribute("teacher", teacher);

        String desc = subject.getDescription();

        model.addAttribute("lectures", subjectService.extract(desc, "#LECTURES"));
        model.addAttribute("labs", subjectService.extract(desc, "#LABS"));
        model.addAttribute("resources", subjectService.extract(desc, "#RESOURCES"));

        Object principal = authentication.getPrincipal();

        if (principal instanceof TeacherDetails teacherDetails) {
            Integer teacherId = teacherDetails.getTeacher().getId();
            boolean isOwner = lessonService.teacherOwnsSubject(teacherId, id);

            model.addAttribute("isOwner", isOwner);
            model.addAttribute("currentTeacher", teacherDetails.getTeacher());
        }

        return "subject";
    }

    @GetMapping("/subjects/edit/{id}")
    public String editSubject(@PathVariable Integer id,
                              Model model,
                              Authentication authentication) {
        Subject subject = subjectService.findById(id);

        Object principal = authentication.getPrincipal();

        if (principal instanceof TeacherDetails teacherDetails) {
            Integer teacherId = teacherDetails.getTeacher().getId();

            if (!lessonService.teacherOwnsSubject(teacherId, id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        model.addAttribute("subjectForm", subject);
        model.addAttribute("departments", departmentService.findAllDepartments());

        return "subject-form";
    }

    @GetMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Integer id) {
        subjectService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/groups/{groupId}/subjects")
    public String getSubjects(@PathVariable Integer groupId,
                              Authentication authentication,
                              Model model) {

        TeacherDetails teacherDetails =
                (TeacherDetails) authentication.getPrincipal();

        Integer teacherId = teacherDetails.getTeacher().getId();

        List<Subject> subjects =
                lessonService.findSubjectsByTeacherAndGroup(teacherId, groupId);

        model.addAttribute("subjects", subjects);

        return "subjects";
    }

    @GetMapping("/lessons/new")
    public String showCreateLessonForm(@ModelAttribute Lesson lesson, Model model) {
        model.addAttribute("lessonForm", lesson);
        model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("days", DayOfWeek.values());

        return "lesson-form";
    }

    @PostMapping("/lessons")
    public String createLesson(@ModelAttribute("lessonForm") Lesson lesson,
                               @RequestParam("subjectId") Integer subjectId,
                               @RequestParam("teacherId") Integer teacherId,
                               @RequestParam("groupId") Integer groupId) {
        lessonService.createLesson(lesson, subjectId, teacherId, groupId);

        return "redirect:/";
    }

    @GetMapping("/lessons/edit/{id}")
    public String editLesson(@PathVariable Integer id, Model model) {
        Lesson lesson = lessonService.findById(id);

        model.addAttribute("lessonForm", lesson);
        model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("days", DayOfWeek.values());

        return "lesson-form";
    }

    @GetMapping("/lessons/delete/{id}")
    public String deleteLesson(@PathVariable Integer id) {
        lessonService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/grades")
    public String getStudentGrades(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof StudentDetails studentDetails) {
            Integer studentId = studentDetails.getStudent().getId();

            model.addAttribute("grades", gradeService.findByStudentId(studentId));

            return "grades";
        }

        return "redirect:/";
    }

    @GetMapping("/grades/new")
    public String showCreateGradesForm(Model model) {
        model.addAttribute("gradeForm", new Grade());
        model.addAttribute("lessonForm", new Lesson());

        return "grade-form";
    }

    @PostMapping("/grades")
    public String createGrade(@ModelAttribute("gradeForm") Grade grade) {
        gradeService.createGrade(grade);

        return "redirect:/";
    }

    @GetMapping("/grades/edit/{id}")
    public String editGrade(@PathVariable Integer id, Model model) {
        Grade grade = gradeService.findById(id);

        model.addAttribute("gradeForm", grade);

        return "grade-form";
    }

    @GetMapping("/grades/delete/{id}")
    public String deleteGrade(@PathVariable Integer id) {
        gradeService.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model, Principal principal) {
        String email = principal.getName();
        Map<DayOfWeek, List<Lesson>> schedule;
        Optional<Student> studentOpt = studentService.findByEmail(email);
        Optional<Teacher> teacherOpt = teacherService.findByEmail(email);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            schedule = lessonService.getScheduleForStudent(student.getId());
            model.addAttribute("isTeacher", false);
        } else if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            schedule = lessonService.getScheduleForTeacher(teacher.getId());
            model.addAttribute("isTeacher", true);
        } else {
            throw new RuntimeException("User not found");
        }

        for (DayOfWeek d : DayOfWeek.values()) {
            schedule.putIfAbsent(d, new ArrayList<>());
        }

        model.addAttribute("schedule", schedule);
        model.addAttribute("timeSlots", List.of(
                "08:00 - 09:35",
                "09:50 - 11:25",
                "11:55 - 13:30",
                "13:45 - 15:20"
        ));
        model.addAttribute("lessonService", lessonService);
        model.addAttribute("days", DayOfWeek.values());

        return "schedule";
    }

    @PostMapping("/search")
    public String searchByRole(@RequestParam("searchTerm") String searchTerm,
                               Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof StudentDetails) {
            List<Teacher> filtered = new ArrayList<>();

            for (Teacher teacher : teacherService.findAllTeachers()) {
                String fullName = (teacher.getName() + " " + teacher.getSurname()).toLowerCase();

                if (fullName.contains(searchTerm.toLowerCase()) ||
                        teacher.getPhoneNumber().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filtered.add(teacher);
                }
            }

            model.addAttribute("teachers", filtered);

            return "teachers";
        }

        if (principal instanceof TeacherDetails) {
            List<Student> filtered = new ArrayList<>();

            for (Student student : studentService.findAllStudents()) {
                String fullName = (student.getName() + " " + student.getSurname()).toLowerCase();

                if (fullName.contains(searchTerm.toLowerCase()) ||
                        student.getPhoneNumber().toLowerCase().contains(searchTerm.toLowerCase())) {

                    filtered.add(student);
                }
            }

            model.addAttribute("students", filtered);

            return "students";
        }

        return "redirect:/";
    }
}