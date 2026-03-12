package com.glebzapara.uniportal.controllers;

import com.glebzapara.uniportal.models.Admin;
import com.glebzapara.uniportal.models.Student;
import com.glebzapara.uniportal.models.Teacher;
import com.glebzapara.uniportal.security.AdminDetails;
import com.glebzapara.uniportal.security.StudentDetails;
import com.glebzapara.uniportal.security.TeacherDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return;

        Object principal = auth.getPrincipal();

        if (principal instanceof AdminDetails adminDetails) {
            Admin admin = adminDetails.getAdmin();
            model.addAttribute("currentAdmin", admin);
        } else if (principal instanceof StudentDetails studentDetails) {
            Student student = studentDetails.getStudent();
            model.addAttribute("currentStudent", student);

        } else if (principal instanceof TeacherDetails teacherDetails) {
            Teacher teacher = teacherDetails.getTeacher();
            model.addAttribute("currentTeacher", teacher);
        }
    }
}