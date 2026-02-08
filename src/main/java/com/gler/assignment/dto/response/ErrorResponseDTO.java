package com.gler.assignment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {
    @Schema(description = "ISO Timestamp")
    private String timestamp;

    @Schema(description = "HTTP Status Code")
    private int status;

    @Schema(description = "Error type")
    private String error;

    @Schema(description = "Detailed error message")
    private String message;

    @Schema(description = "Requested URI path")
    private String path;
}
