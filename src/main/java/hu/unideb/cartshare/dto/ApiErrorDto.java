package hu.unideb.cartshare.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiErrorDto {
    private String message;
}
