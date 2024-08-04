package com.mea.examcontrol.repository.halls.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "halls")
public class HallsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "hallnumber")
    private String hallnumber;

    @Column(name = "description")
    private String description = "";

    @Column(name = "department")
    private String department ;

    @Column(name = "floorname")
    private String floor = "NA";

    @Column(name = "status")
    private int status;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

    @Column(name = "tablecol")
    private int tablecol;

    @Column(name = "tablerow")
    private int tablerow;
    
}
