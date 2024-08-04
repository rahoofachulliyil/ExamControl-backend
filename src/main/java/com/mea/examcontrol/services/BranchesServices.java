package com.mea.examcontrol.services;

import com.mea.examcontrol.model.BranchesModel;
import com.mea.examcontrol.model.DepartmentsModel;
import com.mea.examcontrol.repository.branches.BranchesRepository;
import com.mea.examcontrol.repository.branches.entity.BranchesEntity;
import com.mea.examcontrol.repository.departments.DepartmentsRepository;
import com.mea.examcontrol.repository.departments.entity.DepartmentsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchesServices {

    @Autowired
    BranchesRepository branchesRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean isExistingId(int id) {
        return branchesRepository.existsById(id);
    }

    public ResponseEntity addNewBranch(BranchesModel branchesModel) {

        if (!isExistingId(branchesModel.getId())) {
            BranchesEntity branchesEntity = modelMapper.map(branchesModel, BranchesEntity.class);
            branchesRepository.saveAndFlush(branchesEntity);
            return new ResponseEntity<>("Branch Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to crate, branch already exist", HttpStatus.CONFLICT);
        }
    }

    public BranchesEntity getBranch(Integer id) {
        return branchesRepository.findById(id).get();
    }

    public List<BranchesEntity> getAllBranches() {
        return branchesRepository.findAll();
    }

    public ResponseEntity updateStatus(int id) {
        int status = branchesRepository.findById(id).get().getStatus();
        switch (status){
            case 1:
                status = 0;
                break;
            case 0:
                status =1;
                break;
        }
        branchesRepository.updateStatus(status, id);
        return new ResponseEntity<>("Status Updated", HttpStatus.OK);
    }
}
