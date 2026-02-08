package com.gler.assignment.exception;

import com.gler.assignment.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Specific handling for Task 1 & 2: Validation Errors (400)
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDTO> handleValidationException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Validation failed",
                request.getRequestURI()
        );
    }

    // 2. Specific handling for Task 2: External API Error (502)
    @ExceptionHandler(UpstreamException.class)
    public ResponseEntity<ErrorResponseDTO> handleUpstreamException(UpstreamException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_GATEWAY,
                "Upstream API Unreachable",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // 3. Generic handling for any other exception (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String error, String message, String path) {
        ErrorResponseDTO errorBody = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        return new ResponseEntity<>(errorBody, status);
    }
}