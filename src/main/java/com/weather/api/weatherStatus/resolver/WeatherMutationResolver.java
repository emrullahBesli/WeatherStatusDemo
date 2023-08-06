package com.weather.api.weatherStatus.resolver;

import com.weather.api.weatherStatus.dto.RequestCity;
import com.weather.api.weatherStatus.dto.WeatherDto;
import com.weather.api.weatherStatus.service.WeatherService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeatherMutationResolver implements GraphQLMutationResolver {

    private final WeatherService weatherService;

    public WeatherMutationResolver(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public WeatherDto getWeather(RequestCity requestCity) {
        return weatherService.getWeatherStatusByCity(requestCity);
    }
    public List<WeatherDto> getForecast(RequestCity requestCity){
        return weatherService.getWeatherForecast(requestCity);
    }
    public List<WeatherDto> getForecastDaily( RequestCity requestCity){
        return weatherService.getWeatherForecastDaily(requestCity);
    }
}
