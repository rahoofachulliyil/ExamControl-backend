package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.StudentsModel;
import com.mea.examcontrol.model.UserModel;
import com.mea.examcontrol.repository.students.entity.StudentsEntity;
import com.mea.examcontrol.services.StudentsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("students")
public class StudentsController {

    @Autowired
    StudentsServices studentsServices;

    @GetMapping("/all")
    private List<StudentsEntity> getAllStudents() {
        return studentsServices.getAllStudents();
    }

    @PostMapping(value = "/new")
    private ResponseEntity addNStudent(@RequestBody StudentsModel studentsModel) {
        return studentsServices.addNewStudent(studentsModel);
    }

    @GetMapping("/get/{id}")
    private StudentsEntity getStudent(@PathVariable("id") Integer id) {
        return studentsServices.getStudent(id);
    }

    @PostMapping("/login")
    private ResponseEntity<?> studentLogin(@RequestBody StudentsModel studentsModel) {
        return studentsServices.studentLogin(studentsModel);
    }

    @PostMapping("/status/{id}")
    private ResponseEntity updateStatus(@PathVariable("id") int id) {
        return studentsServices.updateStatus(id);
    }

    @PostMapping(value = "/update")
    private ResponseEntity<?> updateStudent(@RequestBody StudentsModel studentsModel) {
        return studentsServices.updateStudent(studentsModel);
    }

    @PostMapping(value = "/update/secret")
    private ResponseEntity updateStudentSecret(@RequestBody StudentsModel studentsModel) {
        return studentsServices.updateStudentSecret(studentsModel);
    }

    @PostMapping(value = "/generate/secret")
    private ResponseEntity generateStudentSecret(@RequestBody StudentsModel studentsModel) {
        return studentsServices.generateStudentSecret(studentsModel);
    }

}
