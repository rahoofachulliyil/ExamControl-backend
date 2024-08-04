package com.mea.examcontrol.repository.examination.enitiy;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "examination_students")
public class ExaminationStudents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "examid")
    private int examid;

    @Column(name = "name")
    private String name;

    @Column(name = "registernumber")
    private String registernumber;

    @Column(name = "semester")
    private String semester;

    @Column(name = "department")
    private String department;

    @Column(name = "subject")
    private String subject;

    @Column(name = "email")
    private String email;

    @Column(name = "selected")
    private int selected;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

}
