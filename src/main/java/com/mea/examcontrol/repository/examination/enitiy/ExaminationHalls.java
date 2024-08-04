package com.mea.examcontrol.repository.examination.enitiy;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "examination_hall")
public class ExaminationHalls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "examid")
    private int examid;

    @Column(name = "department")
    private String department;

    @Column(name = "hallno")
    private String hallno;

    @Column(name = "tablecol")
    private int tablecol;

    @Column(name = "tablerow")
    private int tablerow;

    @Column(name = "studentsperhall")
    private int studentsperhall;

    @Column(name = "studentperbench")
    private int studentperbench;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

}
