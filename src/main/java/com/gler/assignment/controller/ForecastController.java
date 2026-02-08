package com.gler.assignment.controller;

import com.gler.assignment.dto.response.ErrorResponseDTO;
import com.gler.assignment.dto.request.ForecastRequestDTO;
import com.gler.assignment.service.ForecastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ForecastController {

    private final ForecastService forecastService;

    @Operation(summary = "Fetch and store forecast")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Data processed and saved"), @ApiResponse(responseCode = "400", description = "Mandatory fields missing", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))), @ApiResponse(responseCode = "502", description = "External weather service unreachable", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))), @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))})
    @PostMapping("/forecast")
    public ResponseEntity<Void> getForecast(@Valid @RequestBody ForecastRequestDTO request) {
        forecastService.processForecast(request);
        return ResponseEntity.ok().build();
    }
}
