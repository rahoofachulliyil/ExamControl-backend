package com.mea.examcontrol.repository.examination.enitiy;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "examination_head")
public class ExaminationHeadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "examdate")
    private String examdate;

    @Column(name = "studentsperhall")
    private int studentsperhall;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

    @Column(name = "isgenerated")
    private int generated;

     // ** status: pending, progress, generated, ongoing, closed, suspended
    @Column(name = "status")
    private String status;

    @Column(name = "studentsupload")
    private int studentsupload;

    @Column(name = "staffsupload")
    private int staffsupload;

    @Column(name = "hallsupload")
    private int hallsupload;

    @OneToMany
    @JoinColumn(name = "examid", insertable = false, updatable = false)
    private List<ExaminationStudents> examinationStudents;

    @OneToMany
    @JoinColumn(name = "examid", insertable = false, updatable = false)
    private List<ExaminationStaffs> examinationStaffs;

    @OneToMany
    @JoinColumn(name = "examid", insertable = false, updatable = false)
    private List<ExaminationHalls> examinationHalls;

    @OneToMany
    @JoinColumn(name = "examid", insertable = false, updatable = false)
    private List<ExaminationGenerated> examinationGenerateds;
}
