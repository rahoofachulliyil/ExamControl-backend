package com.mea.examcontrol.repository.halls;

import com.mea.examcontrol.repository.halls.entity.HallsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface HallsRepository extends JpaRepository<HallsEntity, Integer> {

    @Modifying
    @Query("update HallsEntity u set u.status= ?1 where u.id= ?2")
    void updateStatus(int status, int id);

    boolean existsByHallnumber(String hallno);

    HallsEntity getByHallnumber(String hallno);
}
