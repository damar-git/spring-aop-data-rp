package com.damar.spring.repository.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "T_PLAYER_DETAIL")
@Data
public class PlayerDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAYER_DETAIL_ID")
    private Long playerDetailId;

    @Column(name = "TEAM")
    private String team;

    @Column(name = "AGE")
    private Integer age;

}
