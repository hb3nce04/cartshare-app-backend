package hu.unideb.cartshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;

@Mapper(componentModel = "spring", uses = {ListItemMapper.class})
public interface ListMapper {
    List<ListResponseDto> toDtoList(List<hu.unideb.cartshare.model.entity.List> entityList);

    hu.unideb.cartshare.model.entity.List toEntity(ListRequestDto dto);

    ListResponseDto toDto(hu.unideb.cartshare.model.entity.List entity);
}
