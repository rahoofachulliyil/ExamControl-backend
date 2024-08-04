package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.HallsModel;
import com.mea.examcontrol.repository.halls.entity.HallsEntity;
import com.mea.examcontrol.services.HallsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rooms")
public class HallsController {

    @Autowired
    HallsServices hallsServices;

    @GetMapping("/all")
    private List<HallsEntity> getAllHalls() {
        return hallsServices.getAllHalls();
    }

    @PostMapping(value = "/new")
    private ResponseEntity addNHall(@RequestBody HallsModel hallsModel) {
        return hallsServices.addNewHall(hallsModel);
    }

    @PutMapping(value = "/update")
    private ResponseEntity updateHall(@RequestBody HallsModel hallsModel) {
        return hallsServices.updateHall(hallsModel);
    }

    @GetMapping("/get/{id}")
    private HallsEntity getHall(@PathVariable("id") Integer id) {
        return hallsServices.getHall(id);
    }

    @PostMapping("/status/{id}")
    private ResponseEntity updateStatus(@PathVariable("id") int id) {
        return hallsServices.updateStatus(id);
    }
}
