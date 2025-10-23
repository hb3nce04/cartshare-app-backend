package hu.unideb.cartshare.mapper;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User entity);
}
