package com.gler.assignment.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyData {
    private List<Double> temperature_2m;
    private List<Integer> relative_humidity_2m;
    private List<Double> wind_speed_10m;
}
