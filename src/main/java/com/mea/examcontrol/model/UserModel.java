package com.mea.examcontrol.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UserModel {

    private int id;
    private String secret;
    private String name;
    private String role;
    private String mobile;
    private String email;
    private int status;
    private Timestamp createtimestamp;

    private String newsecret;
    private String accessToken;
    private String refreshToken;
    private List<AbilityModel> ability;
}
