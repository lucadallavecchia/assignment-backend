package com.gler.assignment.service;

import com.gler.assignment.dto.external.HourlyData;
import com.gler.assignment.dto.external.OpenMeteoResponse;
import com.gler.assignment.dto.request.ForecastRequestDTO;
import com.gler.assignment.entity.ForecastData;
import com.gler.assignment.exception.UpstreamException;
import com.gler.assignment.repository.ForecastRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForecastServiceImplTest {

    @Mock
    private ForecastRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ForecastServiceImpl forecastService;

    private OpenMeteoResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new OpenMeteoResponse();
        HourlyData hourly = new HourlyData();
        hourly.setTemperature_2m(List.of(10.0, 25.5, 15.0)); // Max: 25.5
        hourly.setRelative_humidity_2m(List.of(40, 80, 60)); // Max: 80
        hourly.setWind_speed_10m(List.of(5.0, 12.2, 8.0));   // Max: 12.2
        mockResponse.setHourly(hourly);
    }

    @Test
    @DisplayName("Should save correctly calculated max values")
    void whenProcessForecast_thenCalculatesMaxAndSaves() {
        ForecastRequestDTO dto = new ForecastRequestDTO(true, true, true);
        when(restTemplate.getForObject(anyString(), eq(OpenMeteoResponse.class))).thenReturn(mockResponse);

        forecastService.processForecast(dto);

        verify(repository, times(1)).save(argThat(entity ->
                entity.getMaxTemperature() == 25.5 &&
                        entity.getMaxHumidity() == 80.0 &&
                        entity.getMaxWindSpeed() == 12.2
        ));
    }

    @Test
    @DisplayName("Should handle null temperature list gracefully")
    void whenTemperatureListIsNull_thenDoesNotSetMaxTemperature() {
        // GIVEN
        ForecastRequestDTO dto = new ForecastRequestDTO(true, false, false);

        OpenMeteoResponse mockResponse = new OpenMeteoResponse();
        HourlyData hourly = new HourlyData();
        hourly.setTemperature_2m(null); // see this
        mockResponse.setHourly(hourly);

        when(restTemplate.getForObject(anyString(), eq(OpenMeteoResponse.class))).thenReturn(mockResponse);

        forecastService.processForecast(dto);

        verify(repository).save(argThat(entity -> entity.getMaxTemperature() == null));
    }

    @Test
    @DisplayName("Should rethrow exception when repository save fails")
    void whenRepositorySaveFails_thenThrowsException() {
        ForecastRequestDTO dto = new ForecastRequestDTO(true, true, true);
        OpenMeteoResponse mockResponse = new OpenMeteoResponse();

        mockResponse.setHourly(new HourlyData());

        when(restTemplate.getForObject(anyString(), eq(OpenMeteoResponse.class))).thenReturn(mockResponse);
        when(repository.save(any(ForecastData.class)))
                .thenThrow(new RuntimeException("Database Connection Error"));
        assertThrows(RuntimeException.class, () -> forecastService.processForecast(dto));

        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should throw UpstreamException when external API is unreachable")
    void whenApiUnreachable_thenThrowsUpstreamException() {
        ForecastRequestDTO dto = new ForecastRequestDTO(true, true, true);
        when(restTemplate.getForObject(anyString(), eq(OpenMeteoResponse.class)))
                .thenThrow(new ResourceAccessException("Connection timeout"));

        assertThrows(UpstreamException.class, () -> forecastService.processForecast(dto));
    }
}