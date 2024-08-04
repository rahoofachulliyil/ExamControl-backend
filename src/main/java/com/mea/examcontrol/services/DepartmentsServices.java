package com.mea.examcontrol.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mea.examcontrol.model.DepartmentsModel;
import com.mea.examcontrol.repository.departments.DepartmentsRepository;
import com.mea.examcontrol.repository.departments.entity.DepartmentsEntity;
import com.mea.examcontrol.repository.users.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DepartmentsServices {

    @Autowired
    DepartmentsRepository departmentsRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean isExistingByName(String name) {
        return departmentsRepository.existsByName(name);
    }

    public ResponseEntity addNewDepartment(DepartmentsModel departmentsModel) {
        if (!isExistingByName(departmentsModel.getName())) {
            DepartmentsEntity departmentsEntity = modelMapper.map(departmentsModel, DepartmentsEntity.class);
            departmentsEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
            departmentsRepository.saveAndFlush(departmentsEntity);
            return new ResponseEntity<>("Department Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to create cause department already exist", HttpStatus.CONFLICT);
        }
    }

    public DepartmentsEntity getDepartment(Integer id) {
        return departmentsRepository.findById(id).get();
    }

    public List<DepartmentsModel> getAllDepartments() {

        List<DepartmentsModel> departmentsModels = new ArrayList<>();
        for(DepartmentsEntity departmentsEntity : departmentsRepository.findAll()){
            DepartmentsModel departmentsModel = modelMapper.map(departmentsEntity, DepartmentsModel.class);
            departmentsModel.setLabel(departmentsEntity.getName());
            departmentsModel.setValue(departmentsEntity.getName());
            departmentsModels.add(departmentsModel);
        }
        return departmentsModels;
    }

    public ResponseEntity updateDepartment(DepartmentsModel departmentsModel) {
        DepartmentsEntity departmentsEntity = departmentsRepository.getById(departmentsModel.getId());
        if(!departmentsEntity.getDescription().equalsIgnoreCase(departmentsModel.getDescription()))
            departmentsEntity.setDescription(departmentsModel.getDescription());
        departmentsRepository.saveAndFlush(departmentsEntity);
        return new ResponseEntity<>("Department Updated Successfully", HttpStatus.OK);
    }
}
