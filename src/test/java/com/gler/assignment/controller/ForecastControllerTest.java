package com.gler.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gler.assignment.dto.request.ForecastRequestDTO;
import com.gler.assignment.exception.UpstreamException;
import com.gler.assignment.service.ForecastService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForecastController.class)
class ForecastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ForecastService forecastService;

    @Test
    @DisplayName("Should return 200 when all mandatory parameters are provided")
    void whenValidRequest_thenReturns200() throws Exception {
        ForecastRequestDTO request = new ForecastRequestDTO(true, true, false);

        mockMvc.perform(post("/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // 2. Missing parameters → 400 Bad Request
    @Test
    @DisplayName("Should return 400 when mandatory parameters are missing")
    void whenMissingParams_thenReturns400() throws Exception {
        // Sending an empty JSON or partial JSON where @NotNull fields are missing
        String invalidJson = "{\"addTemprature\": true}";

        mockMvc.perform(post("/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    // 3. External API unreachable → 502 Upstream API Unreachable
    @Test
    @DisplayName("Should return 502 when service throws UpstreamException")
    void whenServiceThrowsUpstream_thenReturns502() throws Exception {
        ForecastRequestDTO request = new ForecastRequestDTO(true, true, true);

        // Simulating the service failing to reach Open-Meteo
        doThrow(new UpstreamException("Connection to the upstream is unreachable"))
                .when(forecastService).processForecast(any());

        mockMvc.perform(post("/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.error").value("Upstream API Unreachable"));
    }

    // 4. Any other exception → 500 Internal Server Error
    @Test
    @DisplayName("Should return 500 for any other unexpected exception")
    void whenServiceThrowsGenericException_thenReturns500() throws Exception {
        ForecastRequestDTO request = new ForecastRequestDTO(true, true, true);

        doThrow(new RuntimeException("Unexpected DB crash"))
                .when(forecastService).processForecast(any());

        mockMvc.perform(post("/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }
}