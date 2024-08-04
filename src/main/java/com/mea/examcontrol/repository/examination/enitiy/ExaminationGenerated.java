package com.mea.examcontrol.repository.examination.enitiy;

import com.mea.examcontrol.repository.halls.entity.HallsEntity;
import com.mea.examcontrol.repository.students.entity.StudentsEntity;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "examination_generated")
public class ExaminationGenerated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "examid")
    private int examid;

    @Column(name = "department")
    private String department;

    @Column(name = "hall")
    private String hall;

    @Column(name = "staff")
    private String staff;

    @Column(name = "student")
    private String student;

    @Column(name= "subject")
    private String subject;

    @Column(name = "seat")
    private String seat;

    @Column(name = "attendance")
    private int attendance;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hall", referencedColumnName = "hallnumber", insertable = false, updatable = false)
    private HallsEntity halls;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student", referencedColumnName = "registernumber", insertable = false, updatable = false)
    private StudentsEntity students;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "student", referencedColumnName = "subject", insertable = false, updatable = false)
    // private StudentsEntity subject;

}
