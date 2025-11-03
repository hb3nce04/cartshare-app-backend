package hu.unideb.cartshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.model.dto.response.ListResponseDto;

/**
 * Handles mapping betwwen {@link hu.unideb.cartshare.model.entity.List} and {@link ListResponseDto}.
 */
@Mapper(componentModel = "spring", uses = {ListItemMapper.class})
public interface ListMapper {
    List<ListResponseDto> toDtoList(List<hu.unideb.cartshare.model.entity.List> entityList);

    ListResponseDto toDto(hu.unideb.cartshare.model.entity.List entity);
}
