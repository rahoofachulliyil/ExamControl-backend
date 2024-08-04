package com.mea.examcontrol.services;

import com.mea.examcontrol.model.AbilityModel;
import com.mea.examcontrol.model.UserModel;
import com.mea.examcontrol.repository.users.UserRepository;
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
public class UsersServices {

    @Autowired
    UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean isExistingUser(String email) {
        return userRepository.existsByEmail(email);
    }

    public ResponseEntity addNewUser(UserModel userModel) {

        if (!isExistingUser(userModel.getEmail())) {
            UserEntity userEntity = modelMapper.map(userModel, UserEntity.class);
            userEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
            userRepository.saveAndFlush(userEntity);
            return new ResponseEntity<>("Account Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to crate account /n Account already exist", HttpStatus.CONFLICT);
        }
    }

    public UserEntity getUser(Integer id) {
        return userRepository.findById(id).get();
    }

    public ResponseEntity login(UserModel userModel) {
        if (isExistingUser(userModel.getEmail())) {
            UserEntity userEntity = userRepository.getByEmail(userModel.getEmail());
            if (userModel.getSecret().equals(userEntity.getSecret())) {
                UserModel responseUserModel = modelMapper.map(userEntity, UserModel.class);
                AbilityModel abilityModel = new AbilityModel();
                abilityModel.setAction("manage");
                abilityModel.setSubject("all");
                List<AbilityModel> abilityModels = new ArrayList<>();
                abilityModels.add(abilityModel);
                responseUserModel.setAbility(abilityModels);
                return new ResponseEntity<>(responseUserModel, HttpStatus.OK);
            } else
                return new ResponseEntity<>("wrong password", HttpStatus.UNAUTHORIZED);
        } else
            return new ResponseEntity<>("no user found", HttpStatus.CONFLICT);

    }

    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    public ResponseEntity resetPassword(UserModel userModel) {
        UserEntity userEntity = userRepository.findById(userModel.getId()).get();
        if (userEntity.getSecret().equals(userModel.getSecret())) {
            userRepository.resetPassword(userModel.getNewsecret(), userModel.getId());
            return new ResponseEntity<>("Password reset successfull", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong password", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity updateStatus(int id) {
        int status = userRepository.findById(id).get().getStatus();
        switch (status){
            case 1:
                status = 0;
                break;
            case 0:
                status =1;
                break;
        }
        userRepository.updateStatus(status, id);
        return new ResponseEntity<>("Status Updated", HttpStatus.OK);
    }

    public ResponseEntity updateUser(UserModel userModel) {

        UserEntity userEntity = userRepository.getById(userModel.getId());
        if(!userEntity.getMobile().equalsIgnoreCase(userModel.getMobile()))
            userEntity.setMobile(userModel.getMobile());
        if(!userEntity.getName().equalsIgnoreCase(userModel.getName()))
            userEntity.setName(userModel.getName());
        if(userEntity.getStatus() != userModel.getStatus())
            userEntity.setStatus(userModel.getStatus());
        userRepository.saveAndFlush(userEntity);
        return new ResponseEntity<>("Account Updated Successfully", HttpStatus.OK);
    }

    public ResponseEntity updateUserSecret(UserModel userModel) {
        UserEntity userEntity = userRepository.getById(userModel.getId());
        if(userEntity.getSecret().equalsIgnoreCase(userModel.getSecret())){
            return new ResponseEntity<>("Credentials are same", HttpStatus.CONFLICT);
        }else{
            userEntity.setSecret(userModel.getSecret());
            userRepository.saveAndFlush(userEntity);
            return new ResponseEntity<>("Updated Password Successfully", HttpStatus.OK);
        }
    }
}
