package com.mea.examcontrol.repository.examination;

import com.mea.examcontrol.repository.examination.enitiy.ExaminationHalls;
import com.mea.examcontrol.repository.examination.enitiy.ExaminationStaffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ExaminationHallsRepository extends JpaRepository<ExaminationHalls, Integer> {

    List<ExaminationHalls> getByExamid(Integer id);
    void deleteByExamid(Integer id);
}
