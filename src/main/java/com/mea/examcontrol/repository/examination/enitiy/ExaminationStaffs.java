package com.mea.examcontrol.repository.examination.enitiy;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "examination_staffs")
public class ExaminationStaffs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "examid")
    private int examid;

    @Column(name = "name")
    private String name;

    @Column(name = "staffid")
    private String staffid;

    @Column(name = "department")
    private String department;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

}
