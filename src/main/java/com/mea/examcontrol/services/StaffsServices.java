package com.mea.examcontrol.services;

import com.mea.examcontrol.model.StaffsModel;
import com.mea.examcontrol.model.StudentsModel;
import com.mea.examcontrol.repository.staffs.StaffsRepository;
import com.mea.examcontrol.repository.staffs.entity.StaffsEntity;
import com.mea.examcontrol.repository.students.entity.StudentsEntity;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class StaffsServices {

    @Autowired
    StaffsRepository staffsRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean isExistingId(int id) {
        return staffsRepository.existsById(id);
    }

    public ResponseEntity addNewStaff(StaffsModel staffsModel) {
        if (!isExistingId(staffsModel.getId())) {
            StaffsEntity staffsEntity = modelMapper.map(staffsModel, StaffsEntity.class);
            staffsRepository.saveAndFlush(staffsEntity);
            return new ResponseEntity<>("Staff Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to crate, branch already exist", HttpStatus.CONFLICT);
        }
    }

    public StaffsEntity getStaff(Integer id) {
        return staffsRepository.findById(id).get();
    }

    public List<StaffsEntity> getAllStaffs() {
        return staffsRepository.findAll();
    }

    public ResponseEntity updateStatus(int id) {
        int status = staffsRepository.findById(id).get().getStatus();
        switch (status){
            case 1:
                status = 0;
                break;
            case 0:
                status =1;
                break;
        }
        staffsRepository.updateStatus(status, id);
        return new ResponseEntity<>("Status Updated", HttpStatus.OK);
    }

    public boolean isStaffIdExists(String staffid) {
        return staffsRepository.existsByStaffid(staffid);
    }

    public void addStaffFromCSV(CSVRecord csvRecord) {
        StaffsEntity staffsEntity = new StaffsEntity();
        staffsEntity.setName(csvRecord.get(0));
        staffsEntity.setDepartment(csvRecord.get(2));
        staffsEntity.setStaffid(csvRecord.get(1));
        staffsEntity.setStatus(1);
        staffsEntity.setSecret(csvRecord.get(1)+"_"+csvRecord.get(2));
        staffsEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
        staffsRepository.saveAndFlush(staffsEntity);
    }

    public StaffsEntity findByStaffid(String staff) {
        return staffsRepository.findByStaffid(staff);
    }

    public ResponseEntity<?> staffLogin(StaffsModel staffsModel) {

        if(staffsRepository.existsByStaffid(staffsModel.getStaffid())){
            StaffsEntity staffsEntity = staffsRepository.getByStaffid(staffsModel.getStaffid());
            if(staffsEntity.getSecret().equals(staffsModel.getSecret())){
                StaffsModel staffsModel1 = modelMapper.map(staffsEntity, StaffsModel.class);
                return new ResponseEntity<>(staffsModel1, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(staffsModel, HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(staffsModel, HttpStatus.CONFLICT);
        }
    }
}
