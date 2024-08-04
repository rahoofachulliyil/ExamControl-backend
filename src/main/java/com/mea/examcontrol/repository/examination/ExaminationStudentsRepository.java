package com.mea.examcontrol.repository.examination;

import com.mea.examcontrol.repository.examination.enitiy.ExaminationStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ExaminationStudentsRepository extends JpaRepository<ExaminationStudents, Integer> {

    List<ExaminationStudents> findByExamid(int id);

    @Modifying
    @Query("update ExaminationStudents u set u.selected= 0 where u.examid= ?1")
    void updateStudentsSelected(Integer id);
    void deleteByExamid(Integer id);
}
