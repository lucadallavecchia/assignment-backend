package com.gler.assignment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextRequestDTO {

    @Schema(description = "The text to be transformed", minLength = 2, example = "abcde")
    @NotNull
    @Size(min = 2)
    private String text;

}
