package com.mea.examcontrol.repository.examination;

import com.mea.examcontrol.repository.examination.enitiy.ExaminationHeadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ExaminationHeadRepository  extends JpaRepository<ExaminationHeadEntity, Integer> {

    boolean existsByName(String name);

    @Modifying
    @Query("update ExaminationHeadEntity u set u.studentsupload= 1, u.status='progress' where u.id= ?1")
    void updateStudentStatus(Integer id);

    @Modifying
    @Query("update ExaminationHeadEntity u set u.staffsupload= 1, u.status='progress' where u.id= ?1")
    void updateStaffsStatus(Integer id);

    @Modifying
    @Query("update ExaminationHeadEntity u set u.hallsupload= 1, u.status='progress' where u.id= ?1")
    void updateHallsStatus(Integer id);

    @Query(value = "SELECT examdate FROM examination_head where id= ?1", nativeQuery = true)
    String getExamDateById(Integer id);

    @Modifying
    @Query("update ExaminationHeadEntity u set u.generated= 0, u.studentsupload =0, u.staffsupload=0, u.hallsupload=0, u.status='progress' where u.id= ?1")
    void clearStatus(Integer id);

    @Modifying
    @Query("update ExaminationHeadEntity u set u.generated= 2, u.status='closed' where u.id= ?1")
    void closeStatus(Integer id);

    @Modifying
    @Query("update ExaminationHeadEntity u set u.generated= 2, u.status='suspended' where u.id= ?1")
    void suspendStatus(Integer id);
}
