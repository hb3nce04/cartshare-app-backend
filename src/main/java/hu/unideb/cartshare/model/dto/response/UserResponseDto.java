package hu.unideb.cartshare.model.dto.response;

import lombok.*;

/**
 * Response DTO for user registration.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
}
