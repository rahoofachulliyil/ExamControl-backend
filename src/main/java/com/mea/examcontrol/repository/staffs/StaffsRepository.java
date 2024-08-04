package com.mea.examcontrol.repository.staffs;

import com.mea.examcontrol.repository.staffs.entity.StaffsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface StaffsRepository extends JpaRepository<StaffsEntity, Integer> {

    @Modifying
    @Query("update StaffsEntity u set u.status= ?1 where u.id= ?2")
    void updateStatus(int status, int id);

    boolean existsByStaffid(String staffid);

    StaffsEntity findByStaffid(String staff);

    StaffsEntity getByStaffid(String staffid);
}
