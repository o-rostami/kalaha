package com.bol.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Setter
@Getter
@Entity
@Table(name = PlayerEntity.TABLE_NAME)
public class PlayerEntity extends BaseEntity<Long> {

    public static final String TABLE_NAME = "PLAYER";

    //    @Column(name = "FIRST_NAME")
//    private String firstName;
//    @Column(name = "LAST_NAME")
//    private String lastName;
    @Column(name = "USER_NAME")
    private String userName;
//    @Column(name = "PASSWORD")
//    private String password;

//    @NotAudited
//    @OneToMany(mappedBy = "firstPlayer")
//    private Set<GameEntity> firstPlayerGames;
//
//    @NotAudited
//    @OneToMany(mappedBy = "secondPlayer")
//    private Set<GameEntity> secondPlayerGames;

}
