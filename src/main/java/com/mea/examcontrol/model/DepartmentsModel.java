package com.mea.examcontrol.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class DepartmentsModel {

    private int id;
    private String name;
    private String description;
    private int status;
    private Timestamp createtimestamp;
    private String label;
    private String value;
}

