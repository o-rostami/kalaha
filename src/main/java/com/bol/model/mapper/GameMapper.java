package com.bol.model.mapper;

import com.bol.model.dto.GameDto;
import com.bol.model.entity.GameEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameDto entityToDto(GameEntity gameEntity);
}
