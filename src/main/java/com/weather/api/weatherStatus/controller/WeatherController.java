package com.weather.api.weatherStatus.controller;

import com.weather.api.weatherStatus.dto.RequestCity;
import com.weather.api.weatherStatus.dto.WeatherDto;
import com.weather.api.weatherStatus.service.WeatherService;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@EnableScheduling
@RestController
@RequestMapping("/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @Cacheable(value = "getWeather", key = "#city")
    @PostMapping()
    public WeatherDto getWeather(@RequestBody RequestCity city) throws InterruptedException {
        Thread.sleep(5000);
        return weatherService.getWeatherStatusByCity(city);
    }

    @PostMapping("/forecast")
    @Cacheable(value = "forecast", key = "#city")
    public List<WeatherDto> getForecast(@RequestBody RequestCity city) throws InterruptedException {
        Thread.sleep(5000);
        return weatherService.getWeatherForecast(city);

    }

    @PostMapping("/forecastDaily")
    @Cacheable(value = "forecastDaily", key = "#city")
    public List<WeatherDto> getForecastDaily(@RequestBody RequestCity city) throws InterruptedException {
        Thread.sleep(5000);
        return weatherService.getWeatherForecastDaily(city);

    }

    @GetMapping()
    @Cacheable(value = "getWeatherIstanbul")
    public WeatherDto getDefaultWeather() throws InterruptedException {
        Thread.sleep(5000);
        return weatherService.getDefaultWeather();
    }


}
