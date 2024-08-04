package com.mea.examcontrol.repository.students.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "students")
public class StudentsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "fullname")
    private String fullname = "";

    @Column(name = "registernumber")
    private String registernumber;

    @Column(name = "address")
    private String address = "";

    @Column(name = "yearofjoin")
    private String yearofjoin = "";

    @Column(name = "department")
    private String department = "";

    @Column(name = "semseter")
    private String semseter = "";

    @Column(name = "mobile")
    private String mobile = "";

    @Column(name = "email")
    private String email = "";

    @Column(name = "photo")
    private String photo = "";

    @Column(name = "status")
    private int status;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

    @Column(name= "secret")
    private String secret;

}
