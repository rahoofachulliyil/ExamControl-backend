package com.mea.examcontrol.model;

import lombok.Data;

import java.io.File;

@Data
public class ReferenceModel {

    private int id;
    private String type;
    private String description;
    private String department;
    private String semester;
    private String subject;
    private String location;
    private int year;
    private int status;
    private File file;
    private String filename;
}
