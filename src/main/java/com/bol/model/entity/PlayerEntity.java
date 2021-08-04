package com.bol.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Audited
@Setter
@Getter
@Entity
@Table(name = PlayerEntity.TABLE_NAME)
public class PlayerEntity extends BaseEntity<Long> {

    public static final String TABLE_NAME = "PLAYER";

    @Column(name = "USER_NAME")
    private String userName;

    @NotAudited
    @OneToMany(mappedBy = "firstPlayer")
    private Set<GameEntity> firstPlayerGames;

    @NotAudited
    @OneToMany(mappedBy = "secondPlayer")
    private Set<GameEntity> secondPlayerGames;

}
