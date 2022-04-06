package com.workshop.projectmanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "user")
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public UserEntity() {
    }

    public UserEntity(Integer id) {
        this.id = id;
    }
}
