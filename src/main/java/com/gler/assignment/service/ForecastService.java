package com.gler.assignment.service;

import com.gler.assignment.dto.request.ForecastRequestDTO;

public interface ForecastService {

    /**
     * Process the forecast request based on the provided DTO.
     *
     * @param dto the forecast request data transfer object
     */
    void processForecast(ForecastRequestDTO dto);
}
