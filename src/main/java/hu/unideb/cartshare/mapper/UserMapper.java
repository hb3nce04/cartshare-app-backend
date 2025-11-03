package hu.unideb.cartshare.mapper;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.model.entity.User;

/**
 * Handles mapping betwwen {@link hu.unideb.cartshare.model.entity.User} and {@link hu.unideb.cartshare.model.dto.response.UserResponseDto}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User entity);
}
