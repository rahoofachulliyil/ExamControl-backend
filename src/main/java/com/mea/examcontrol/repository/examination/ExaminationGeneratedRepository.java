package com.mea.examcontrol.repository.examination;

import com.mea.examcontrol.model.ExamModel;
import com.mea.examcontrol.repository.examination.enitiy.ExaminationGenerated;
import com.mea.examcontrol.repository.examination.enitiy.ExaminationHalls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ExaminationGeneratedRepository extends JpaRepository<ExaminationGenerated, Integer> {

    List<ExaminationGenerated> getByExamid(Integer id);

    @Query(value = "select examid, department, hall, staff, student, seat, attendance, `name` as examname, `description`, examdate, isgenerated, `status`  from examination_generated eg, examination_head eh \n" +
            "where eg.examid = eh.id and student = ?1 order by examdate desc limit 10", nativeQuery = true)
    List<Object[]> getExamHistoryByStudent(String regno);

    @Query(value = "select examid, department, hall, staff, student, seat, attendance, `name` as examname, `description`, examdate, isgenerated, `status` \n" +
            "from examination_generated eg, examination_head eh where eg.examid = eh.id and staff = ?1 and status = 'ongoing' order by examdate", nativeQuery = true)
    List<Object[]> getExamHistoryByStaff(String staffid);


    void deleteByExamid(Integer id);

    @Modifying
     @Query("update ExaminationGenerated u set u.attendance= ?1 where u.examid = ?2 and u.student = ?3")
     void updateAttendance(int attendance, int examid, String student);
}
