package com.weather.api.weatherStatus.resolver;

import com.weather.api.weatherStatus.dto.WeatherDto;
import com.weather.api.weatherStatus.service.WeatherService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;



@Component
public class WeatherResolver implements GraphQLQueryResolver {

    private final WeatherService weatherService;

    public WeatherResolver(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    public WeatherDto getDefaultWeather() {
        return weatherService.getDefaultWeather();
    }

    public String hello(){
    return "Hello";
    }
}
