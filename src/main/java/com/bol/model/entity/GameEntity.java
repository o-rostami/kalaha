package com.bol.model.entity;

import com.bol.model.enums.DifficultyLevel;
import com.bol.model.enums.GameStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

@Audited
@Setter
@Getter
@Entity
@Table(name = GameEntity.TABLE_NAME)
public class GameEntity extends BaseEntity<Long> {

    public static final String TABLE_NAME = "GAME";

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIRST_PLAYER", referencedColumnName = "ID")
    private PlayerEntity firstPlayer;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECOND_PLAYER", referencedColumnName = "ID")
    private PlayerEntity secondPlayer;

    @NotAudited
    @OneToOne
    @JoinColumn(name = "BOARD_ID", referencedColumnName = "ID")
    private BoardEntity board;

    @Column(name = "WINNER")
    private String winner;

    @Column(name = "STATUS")
    @Convert(converter = GameStatus.GameStatusConverter.class)
    private GameStatus status;

    @Column(name = "DIFFICULTY_LEVEL")
    @Convert(converter = DifficultyLevel.DifficultyLevelConverter.class)
    private DifficultyLevel difficultyLevel;

    @Column(name = "PLAYER_TURN")
    private String playerTurn;

    @Transient
    private Integer currentPitIndex;


}
