package com.mea.examcontrol.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StaffsModel {

    private int id;
    private String staffid;
    private String name;
    private String yearofjoin;
    private String mobile;
    private String email;
    private int status;
    private Timestamp createtimestamp;
    private String department;

    private String secret;
}
