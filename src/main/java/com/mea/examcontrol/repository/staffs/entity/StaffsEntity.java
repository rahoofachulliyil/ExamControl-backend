package com.mea.examcontrol.repository.staffs.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "staffs")
public class StaffsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "staffid")
    private String staffid;

    @Column(name = "name")
    private String name = "";

    @Column(name = "yearofjoin")
    private String yearofjoin = "";

    @Column(name = "department")
    private String department = "";

    @Column(name = "mobile")
    private String mobile = "";

    @Column(name = "email")
    private String email = "";

    @Column(name = "status")
    private int status;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

    @Column(name= "secret")
    private String secret;
}
