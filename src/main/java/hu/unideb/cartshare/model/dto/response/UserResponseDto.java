package hu.unideb.cartshare.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response DTO for user registration.
 */
@Getter
@Setter
@Builder
public class UserResponseDto {
    private String username;
    private String email;
}
