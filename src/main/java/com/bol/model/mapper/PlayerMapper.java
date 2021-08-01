package com.bol.model.mapper;

import com.bol.model.dto.PlayerDto;
import com.bol.model.entity.PlayerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerEntity dtoToEntity(PlayerDto dto);

    PlayerDto entityToDto(PlayerEntity entity);

}
