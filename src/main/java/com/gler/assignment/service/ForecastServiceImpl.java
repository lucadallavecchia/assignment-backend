package com.gler.assignment.service;

import com.gler.assignment.dto.request.ForecastRequestDTO;
import com.gler.assignment.dto.external.OpenMeteoResponse;
import com.gler.assignment.entity.ForecastData;
import com.gler.assignment.exception.UpstreamException;
import com.gler.assignment.repository.ForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository repository;
    private final RestTemplate restTemplate;

    @Override
    public void processForecast(ForecastRequestDTO dto) {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m";

        try {
            OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

            if (response == null || response.getHourly() == null) {
                throw new RuntimeException("Empty response from Open-Meteo");
            }

            ForecastData entity = ForecastData.builder()
                    .date(LocalDate.now())
                    .build();

            // 1. Temperature - Max calculation using Streams
            if (Boolean.TRUE.equals(dto.getAddTemprature())) {
                Optional.ofNullable(response.getHourly().getTemperature_2m())
                        .flatMap(list -> list.stream().max(Double::compare))
                        .ifPresent(entity::setMaxTemperature);
            }

            // 2. Humidity - Max calculation (Integer to Double conversion)
            if (Boolean.TRUE.equals(dto.getAddHumidity())) {
                Optional.ofNullable(response.getHourly().getRelative_humidity_2m())
                        .flatMap(list -> list.stream().max(Integer::compare))
                        .map(Integer::doubleValue) // Convert Integer to Double
                        .ifPresent(entity::setMaxHumidity);
            }

            // 3. Wind Speed - Max calculation
            if (Boolean.TRUE.equals(dto.getAddWindSpeed())) {
                Optional.ofNullable(response.getHourly().getWind_speed_10m())
                        .flatMap(list -> list.stream().max(Double::compare))
                        .ifPresent(entity::setMaxWindSpeed);
            }

            repository.save(entity);
            log.info("Forecast data saved successfully for date: {}", entity.getDate());

        } catch (ResourceAccessException e) {
            log.error("External API unreachable: {}", e.getMessage());
            throw new UpstreamException("Connection to the upstream is unreachable");
        } catch (Exception e) {
            log.error("Internal error during forecast processing", e);
            throw e;
        }
    }
}
