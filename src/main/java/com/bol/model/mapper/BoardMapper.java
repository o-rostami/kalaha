package com.bol.model.mapper;

import com.bol.model.dto.BoardDto;
import com.bol.model.entity.BoardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardDto entityToDto(BoardEntity entity);


}
