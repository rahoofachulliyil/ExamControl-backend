package com.mea.examcontrol.model;

import com.mea.examcontrol.repository.examination.enitiy.ExaminationGenerated;
import com.mea.examcontrol.repository.halls.entity.HallsEntity;
import com.mea.examcontrol.repository.staffs.entity.StaffsEntity;
import lombok.Data;

import java.util.List;

@Data
public class HallWiseArrangements {

    private String hall;
    private int examid;
    private String examdate;
    private HallsEntity hallsEntity;
    private StaffsEntity staffsEntity;
    private List<ExaminationGenerated> examinationGenerated;

}
