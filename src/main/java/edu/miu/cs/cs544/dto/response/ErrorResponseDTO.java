package edu.miu.cs.cs544.dto.response;

import lombok.Data;

@Data
public class ErrorResponseDTO implements ResponseDTO{
    private String message;
    private int statusCode;

    public ErrorResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
