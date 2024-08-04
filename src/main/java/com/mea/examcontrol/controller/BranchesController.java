package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.BranchesModel;
import com.mea.examcontrol.model.DepartmentsModel;
import com.mea.examcontrol.repository.branches.entity.BranchesEntity;
import com.mea.examcontrol.repository.departments.entity.DepartmentsEntity;
import com.mea.examcontrol.services.BranchesServices;
import com.mea.examcontrol.services.DepartmentsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("branches")
public class BranchesController {

    @Autowired
    BranchesServices branchesServices;

    @GetMapping("/all")
    private List<BranchesEntity> getAllBranches() {
        return branchesServices.getAllBranches();
    }

    @PostMapping(value = "/new")
    private ResponseEntity addNewBranch(@RequestPart("department") BranchesModel branchesModel) {
        return branchesServices.addNewBranch(branchesModel);
    }

    @GetMapping("/get/{id}")
    private BranchesEntity getBranch(@PathVariable("id") Integer id) {
        return branchesServices.getBranch(id);
    }

    @PostMapping("/status/{id}")
    private ResponseEntity updateStatus(@PathVariable("id") int id) {
        return branchesServices.updateStatus(id);
    }
}
