package com.mea.examcontrol.model;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class ExaminationModel {

    private int id;
    private String name;
    private String description;
    private String examdate;
    private int studentsperhall;
    private int generated;
    // ** status: pending, progress, generated, ongoing, closed, suspended
    private String status;
    private Timestamp createtimestamp;
    private int studentsupload;
    private int staffsupload;
    private int hallsupload;
}
