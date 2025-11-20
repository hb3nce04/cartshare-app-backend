package hu.unideb.cartshare.mapper;

import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.model.entity.User;
import org.mapstruct.Mapper;

/**
 * Handles mapping betwwen {@link hu.unideb.cartshare.model.entity.User}
 * and {@link hu.unideb.cartshare.model.dto.response.UserResponseDto}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User entity);
}
