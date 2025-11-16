package hu.unideb.cartshare.mapper;

import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;
import org.mapstruct.Mapper;

import java.util.Set;

/**
 * Handles mapping betwwen {@link hu.unideb.cartshare.model.entity.ListItem}
 * and {@link hu.unideb.cartshare.model.dto.response.ListItemResponseDto}.
 */
@Mapper(componentModel = "spring")
public interface ListItemMapper {
    ListItemResponseDto toDto(ListItem entity);

    Set<ListItemResponseDto> toDtoSet(Set<ListItem> entitySet);
}
