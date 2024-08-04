package com.mea.examcontrol.repository.users;

import com.mea.examcontrol.repository.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByEmail(String email);

    UserEntity getByEmail(String email);

    @Modifying
    @Query("update UserEntity u set u.secret = ?1 where u.id = ?2")
    void resetPassword(String newsecret, int id);

    @Modifying
    @Query("update UserEntity u set u.status= ?1 where u.id= ?2")
    void updateStatus(int status, int id);

}
