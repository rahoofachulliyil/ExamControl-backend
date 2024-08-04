package com.mea.examcontrol.repository.branches;

import com.mea.examcontrol.repository.branches.entity.BranchesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BranchesRepository extends JpaRepository<BranchesEntity, Integer> {

    @Modifying
    @Query("update BranchesEntity u set u.status= ?1 where u.id= ?2")
    void updateStatus(int status, int id);
}
