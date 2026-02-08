package com.gler.assignment.controller;


import com.gler.assignment.dto.response.ErrorResponseDTO;
import com.gler.assignment.dto.request.TextRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextReplaceController {

    @Operation(summary = "Replace text boundaries", description = "Transforms the string by replacing the first character with '*' and the last with '$'. " + "If length is exactly 2, returns an empty string.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful transformation"), @ApiResponse(responseCode = "400", description = "Invalid input - text length must be at least 2", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))), @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))})
    @GetMapping("/replace")
    public ResponseEntity<String> replaceText(@Valid TextRequestDTO request) {
        String text = request.getText();

        if (text.length() == 2) {
            return ResponseEntity.ok("");
        }

        String result = "*" + text.substring(1, text.length() - 1) + "$";

        return ResponseEntity.ok(result);
    }
}
