package hu.unideb.cartshare.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;

/**
 * Handles mapping betwwen {@link hu.unideb.cartshare.model.entity.ListItem} and {@link hu.unideb.cartshare.model.dto.response.ListItemResponseDto}.
 */
@Mapper(componentModel = "spring")
public interface ListItemMapper {
    ListItemResponseDto toDto(ListItem entity);

    Set<ListItemResponseDto> toDtoSet(Set<hu.unideb.cartshare.model.entity.ListItem> entitySet);
}
