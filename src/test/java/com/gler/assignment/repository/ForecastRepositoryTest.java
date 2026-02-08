package com.gler.assignment.repository;

import com.gler.assignment.entity.ForecastData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ForecastRepositoryTest {

    @Autowired
    private ForecastRepository repository;

    @Test
    @DisplayName("Should persist ForecastData entity")
    void whenSave_thenEntityIsPersisted() {
        ForecastData data = ForecastData.builder()
                .date(LocalDate.now())
                .maxTemperature(20.0)
                .build();

        ForecastData saved = repository.save(data);

        assertNotNull(saved.getId());
    }
}
