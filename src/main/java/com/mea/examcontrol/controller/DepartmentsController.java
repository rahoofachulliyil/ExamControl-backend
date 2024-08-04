package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.DepartmentsModel;
import com.mea.examcontrol.repository.departments.entity.DepartmentsEntity;
import com.mea.examcontrol.services.DepartmentsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departments")
public class DepartmentsController {

    @Autowired
    DepartmentsServices departmentsServices;

    @GetMapping("/all")
    private List<DepartmentsModel> getAllDepartments() {
        return departmentsServices.getAllDepartments();
    }

    @PostMapping(value = "/add")
    private ResponseEntity addDepartment(@RequestBody DepartmentsModel departmentsModel) {
        return departmentsServices.addNewDepartment(departmentsModel);
    }

    @PostMapping(value = "/update")
    private ResponseEntity updateDepartment(@RequestBody DepartmentsModel departmentsModel) {
        return departmentsServices.updateDepartment(departmentsModel);
    }

    @GetMapping("/get/{id}")
    private DepartmentsEntity getDepartment(@PathVariable("id") Integer id) {
        return departmentsServices.getDepartment(id);
    }

}
