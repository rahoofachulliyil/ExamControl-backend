package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.StaffsModel;
import com.mea.examcontrol.model.StudentsModel;
import com.mea.examcontrol.repository.staffs.entity.StaffsEntity;
import com.mea.examcontrol.services.StaffsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("staff")
public class StaffsController {

    @Autowired
    StaffsServices staffsServices;

    @GetMapping("/all")
    private List<StaffsEntity> getAllStaffs() {
        return staffsServices.getAllStaffs();
    }

    @PostMapping(value = "/add")
    private ResponseEntity addNStaff(@RequestBody StaffsModel staffsModel) {
        return staffsServices.addNewStaff(staffsModel);
    }

    @GetMapping("/get/{id}")
    private StaffsEntity getStaff(@PathVariable("id") Integer id) {
        return staffsServices.getStaff(id);
    }

    @PostMapping("/status/{id}")
    private ResponseEntity updateStatus(@PathVariable("id") int id) {
        return staffsServices.updateStatus(id);
    }

    @PostMapping("/login")
    private ResponseEntity<?> staffLogin(@RequestBody StaffsModel staffsModel) {
        return staffsServices.staffLogin(staffsModel);
    }
}
