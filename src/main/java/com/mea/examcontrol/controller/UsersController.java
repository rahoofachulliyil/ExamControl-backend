package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.UserModel;
import com.mea.examcontrol.repository.users.entity.UserEntity;
import com.mea.examcontrol.services.UsersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UsersController {

    @Autowired
    UsersServices userService;

    @GetMapping("/all")
    private List<UserEntity> getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping(value = "/add")
    private ResponseEntity addUser(@RequestBody UserModel userModel) {
        return userService.addNewUser(userModel);
    }

    @PostMapping(value = "/update")
    private ResponseEntity updateUser(@RequestBody UserModel userModel) {
        return userService.updateUser(userModel);
    }

    @PostMapping(value = "/update/secret")
    private ResponseEntity updateUserSecret(@RequestBody UserModel userModel) {
        return userService.updateUserSecret(userModel);
    }

    @GetMapping("/get/{id}")
    private UserEntity getUser(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }

    @PostMapping("/login")
    private ResponseEntity login(@RequestBody UserModel userModel) {
        return userService.login(userModel);
    }

}
