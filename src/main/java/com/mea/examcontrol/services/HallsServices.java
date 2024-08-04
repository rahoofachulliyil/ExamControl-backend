package com.mea.examcontrol.services;

import com.mea.examcontrol.model.HallsModel;
import com.mea.examcontrol.repository.halls.HallsRepository;
import com.mea.examcontrol.repository.halls.entity.HallsEntity;
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
public class HallsServices {

    @Autowired
    HallsRepository hallsRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean isExistingId(int id) {
        return hallsRepository.existsById(id);
    }

    public ResponseEntity addNewHall(HallsModel hallsModel) {

        if (!isExistingId(hallsModel.getId())) {
            HallsEntity hallsEntity = modelMapper.map(hallsModel, HallsEntity.class);
            hallsRepository.saveAndFlush(hallsEntity);
            return new ResponseEntity<>("Hall Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to crate hall already exist", HttpStatus.CONFLICT);
        }
    }

    public HallsEntity getHall(Integer id) {
        return hallsRepository.findById(id).get();
    }

    public List<HallsEntity> getAllHalls() {
        return hallsRepository.findAll();
    }

    public ResponseEntity updateStatus(int id) {
        int status = hallsRepository.findById(id).get().getStatus();
        switch (status){
            case 1:
                status = 0;
                break;
            case 0:
                status =1;
                break;
        }
        hallsRepository.updateStatus(status, id);
        return new ResponseEntity<>("Status Updated", HttpStatus.OK);
    }

    public boolean isHallnoExists(String hallno) {
        return hallsRepository.existsByHallnumber(hallno);
    }

    public void addHallFromCSV(CSVRecord csvRecord) {
        HallsEntity hallsEntity = new HallsEntity();
        hallsEntity.setHallnumber(csvRecord.get(1));
        hallsEntity.setDescription(csvRecord.get(0)+" - "+csvRecord.get(1));
        hallsEntity.setDepartment(csvRecord.get(0));
        hallsEntity.setTablecol(Integer.parseInt(csvRecord.get(2)));
        hallsEntity.setTablerow(Integer.parseInt(csvRecord.get(3)));
        hallsEntity.setStatus(1);
        hallsEntity.setFloor("NA");
        hallsEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
        hallsRepository.saveAndFlush(hallsEntity);
    }

    public HallsEntity getByHallnumber(String hallno) {
        return hallsRepository.getByHallnumber(hallno);
    }

    public ResponseEntity updateHall(HallsModel hallsModel) {
        HallsEntity hallsEntity = hallsRepository.findById(hallsModel.getId()).get();
        if(hallsEntity.getTablecol() != hallsModel.getTablecol() && hallsModel.getTablecol() != 0){
            hallsEntity.setTablecol(hallsModel.getTablecol());
        }
        if(hallsEntity.getTablerow() != hallsModel.getTablerow() && hallsModel.getTablerow() != 0){
            hallsEntity.setTablerow(hallsModel.getTablerow());
        }
        hallsRepository.saveAndFlush(hallsEntity);
        return new ResponseEntity<>("Hall Updated Successfully", HttpStatus.ACCEPTED);
    }
}
