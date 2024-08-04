package com.mea.examcontrol.repository.departments;

import com.mea.examcontrol.repository.departments.entity.DepartmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface DepartmentsRepository extends JpaRepository<DepartmentsEntity, Integer> {

    boolean existsByName(String name);

}
