package com.bol.model.dto;

import com.bol.model.enums.GameStatus;
import lombok.Data;

@Data
public class GameDto extends BaseDto {

    private PlayerDto firstPlayer;
    private PlayerDto secondPlayer;
    private BoardDto board;
    private GameStatus status;
    private String winner;
    private String playerTurn;

}
