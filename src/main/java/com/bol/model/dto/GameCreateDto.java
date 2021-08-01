package com.bol.model.dto;

import com.bol.model.enums.DifficultyLevel;
import lombok.Data;

@Data
public class GameCreateDto {

    private PlayerDto firstPlayer;
    private DifficultyLevel difficultyLevel;

}
