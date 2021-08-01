package com.bol.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Audited
@Setter
@Getter
@Entity
@Table(name = BoardEntity.TABLE_NAME)
public class BoardEntity extends BaseEntity<Long> {

    public static final String TABLE_NAME = "BOARD";


    @NotAudited
    @OneToOne(mappedBy = "board")
    private GameEntity gameEntity;

    @Column(name = "FIRST_PIT_PLAYER_A")
    private Integer firstPitPlayerA;
    @Column(name = "SECOND_PIT_PLAYER_A")
    private Integer secondPitPlayerA;
    @Column(name = "THIRD_PIT_PLAYER_A")
    private Integer thirdPitPlayerA;
    @Column(name = "FORTH_PIT_PLAYER_A")
    private Integer forthPitPlayerA;
    @Column(name = "FIFTH_PIT_PLAYER_A")
    private Integer fifthPitPlayerA;
    @Column(name = "SIX_PIT_PLAYER_A")
    private Integer sixthPitPlayerA;
    @Column(name = "RIGHT_PIT_HOUSE_ID")
    private Integer rightPitHouseId;
    @Column(name = "FIRST_PIT_PLAYER_B")
    private Integer firstPitPlayerB;
    @Column(name = "SECOND_PIT_PLAYER_B")
    private Integer secondPitPlayerB;
    @Column(name = "THIRD_PIT_PLAYER_B")
    private Integer thirdPitPlayerB;
    @Column(name = "FORTH_PIT_PLAYER_B")
    private Integer forthPitPlayerB;
    @Column(name = "FIFTH_PIT_PLAYER_B")
    private Integer fifthPitPlayerB;
    @Column(name = "SIX_PIT_PLAYER_B")
    private Integer sixthPitPlayerB;
    @Column(name = "LEFT_PIT_HOUSE_ID")
    private Integer leftPitHouseId;



}
