package com.mea.examcontrol.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;

@Data
public class StudentsModel {

    private int id;
    private String fullname;
    private String registernumber;
    private String address;
    private String yearofjoin;
    private String department;
    private String semseter;
    private String mobile;
    private String email;
    private String photo;
    private int status;
    private Timestamp createtimestamp;

    private String secret;
}
