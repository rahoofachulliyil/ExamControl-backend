package com.mea.examcontrol.repository.examination;

import com.mea.examcontrol.repository.examination.enitiy.ExaminationStaffs;
import com.mea.examcontrol.repository.examination.enitiy.ExaminationStudents;
import com.mea.examcontrol.repository.staffs.entity.StaffsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ExaminationStaffsRepository extends JpaRepository<ExaminationStaffs, Integer> {

    List<ExaminationStaffs> findByExamid(Integer id);
    void deleteByExamid(Integer id);
}
