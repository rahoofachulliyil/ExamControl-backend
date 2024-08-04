package com.mea.examcontrol.repository.users.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "secret")
    private String secret;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private int status;

    @Column(name = "createtimestamp")
    private Timestamp createtimestamp;

}
