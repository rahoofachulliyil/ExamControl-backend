package com.mea.examcontrol.model;

import com.mea.examcontrol.repository.examination.enitiy.ExaminationGenerated;
import com.mea.examcontrol.repository.examination.enitiy.ExaminationStudents;
import lombok.Data;

import java.util.Date;

@Data
public class ExamModel {

    private int examid,attendance,isgenerated;
    private String department, hall, staff, student, seat, examname, description, examdate, status;
}
