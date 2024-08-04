package com.mea.examcontrol.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class HallsModel {

    private int id;
    private String hallnumber;
    private String description;
    private int departmentid;
    private String floor;
    private int status;
    private Timestamp createtimestamp;

    private int tablecol;
    private int tablerow;
    private int benches;
}

