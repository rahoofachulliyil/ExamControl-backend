package com.mea.examcontrol.repository.branches.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "branches")
public class BranchesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "departmentid")
    private int departmentid;

    @Column(name = "status")
    private int status;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;
}
