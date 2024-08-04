package com.mea.examcontrol.repository.reference;

import com.mea.examcontrol.repository.reference.entity.ReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReferenceRepository extends JpaRepository<ReferenceEntity, Integer> {

    List<ReferenceEntity> findByType(String type);

    @Modifying
    @Query("update ReferenceEntity u set u.status= ?1 where u.id= ?2")
    void updateStatus(int status, int id);
}
