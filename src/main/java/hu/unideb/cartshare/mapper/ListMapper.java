package hu.unideb.cartshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.dto.response.ListResponseDto;

@Mapper(componentModel = "spring", uses = {ListItemMapper.class})
public interface ListMapper {
    List<ListResponseDto> toDto(List<hu.unideb.cartshare.entity.List> list);
}
