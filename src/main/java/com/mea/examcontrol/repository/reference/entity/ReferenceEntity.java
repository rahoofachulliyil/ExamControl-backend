package com.mea.examcontrol.repository.reference.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "reference_data")
public class ReferenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "reftype")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "department")
    private String department;

    @Column(name = "semester")
    private String semester;

    @Column(name = "subject")
    private String subject;

    @Column(name = "location")
    private String location;

    @Column(name = "year")
    private int year;

    @Column(name = "status")
    private int status;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

}
