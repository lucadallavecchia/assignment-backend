package com.gler.assignment.repository;

import com.gler.assignment.entity.ForecastData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForecastRepository extends JpaRepository<ForecastData, Long> {}
