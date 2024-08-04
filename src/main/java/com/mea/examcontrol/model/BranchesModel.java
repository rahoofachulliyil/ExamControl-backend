package com.mea.examcontrol.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BranchesModel {

    private int id;
    private String name;
    private String description;
    private int departmentid;
    private int status;
    private Timestamp createtimestamp;
}
