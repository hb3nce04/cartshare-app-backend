package hu.unideb.cartshare.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response DTO for the unified error responses.
 */
@Getter
@Setter
@Builder
public class ApiErrorResponseDto {
    private String message;
}
