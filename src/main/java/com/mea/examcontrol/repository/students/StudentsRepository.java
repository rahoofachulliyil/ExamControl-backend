package com.mea.examcontrol.repository.students;

import com.mea.examcontrol.repository.students.entity.StudentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface StudentsRepository extends JpaRepository<StudentsEntity, Integer> {

    @Modifying
    @Query("update StudentsEntity u set u.status= ?1 where u.id= ?2")
    void updateStatus(int status, int id);

    boolean existsByRegisternumber(String registernumber);

    StudentsEntity getByRegisternumber(String registernumber);

    @Modifying
    @Query("update StudentsEntity u set u.secret= ?1 where u.registernumber= ?2")
    void updateSecret(String secret, String registernumber);
}
