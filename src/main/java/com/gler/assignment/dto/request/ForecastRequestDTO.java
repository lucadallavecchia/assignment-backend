package com.gler.assignment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastRequestDTO {

    @NotNull(message = "addTemprature is mandatory")
    private Boolean addTemprature;

    @NotNull(message = "addHumidity is mandatory")
    private Boolean addHumidity;

    @NotNull(message = "addWindSpeed is mandatory")
    private Boolean addWindSpeed;
}
