package com.weather.api.weatherStatus.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.api.weatherStatus.dto.RequestCity;
import com.weather.api.weatherStatus.dto.WeatherDto;
import com.weather.api.weatherStatus.exception.CityCouldNotNullException;
import com.weather.api.weatherStatus.exception.CityNotFoundException;
import com.weather.api.weatherStatus.model.UserRequest;
import com.weather.api.weatherStatus.repository.RequestRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class WeatherService {
    private static final String weatherUrl = "http://api.openweathermap.org/data/2.5/weather";
    private static final String weatherUrlForecast = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private final RequestRepository requestRepository;
    @Value("${apiKey.book.key}")
    private String API_KEY;

    public WeatherService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public JsonNode apiRequestBuilder(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);


        return jsonNode;

    }


    public WeatherDto getWeatherStatusByCity(RequestCity city) {
        String url = weatherUrl + "?q=" + city.getCity() + "&units=metric&appid=" + API_KEY + "&lang=tr";


        try {
            JsonNode jsonNode = apiRequestBuilder(url);


            String cityName = jsonNode.get("name").asText();


            double temperature = jsonNode.get("main").get("temp").asDouble();


            long timestamp = jsonNode.get("dt").asLong();
            Date date = new Date(timestamp * 1000); // Saniyeleri milisaniyelere çevirin
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String dateTime = sdf.format(date);


            String weatherDescription = jsonNode.get("weather").get(0).get("description").asText();

            String country = jsonNode.get("sys").get("country").asText();


            UserRequest userRequest = new UserRequest(city.getCity(), country, LocalDateTime.now());
            requestRepository.save(userRequest);
            WeatherDto weatherDto = new WeatherDto(cityName, country, temperature, dateTime, weatherDescription);
            return weatherDto;


        } catch (Exception e) {
            if (city.getCity().isEmpty() || city.getCity().isBlank()) {
                throw new CityCouldNotNullException("City could not blank:");

            } else {
                throw new CityNotFoundException("City could not found : " + city.getCity());
            }


        }


    }

    public WeatherDto getDefaultWeather() {

        String city = new RequestCity().getCity();
        String url = weatherUrl + "?q=" + city + "&units=metric&appid=" + API_KEY + "&lang=tr";

        try {
            JsonNode jsonNode = apiRequestBuilder(url);


            String cityName = jsonNode.get("name").asText();


            double temperature = jsonNode.get("main").get("temp").asDouble();


            long timestamp = jsonNode.get("dt").asLong();
            Date date = new Date(timestamp * 1000); // Saniyeleri milisaniyelere çevirin
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String dateTime = sdf.format(date);


            String weatherDescription = jsonNode.get("weather").get(0).get("description").asText();

            String country = jsonNode.get("sys").get("country").asText();


            WeatherDto weatherDto = new WeatherDto(cityName, country, temperature, dateTime, weatherDescription);
            return weatherDto;


        } catch (Exception e) {
            throw new RuntimeException("Hata oluştu");


        }


    }


    public List<WeatherDto> getWeatherForecast(RequestCity city) {
        String url = weatherUrlForecast + city.getCity() + "&units=metric&cnt=7&appid=" + API_KEY + "&lang=tr";


        List<WeatherDto> list = new ArrayList<>();
        String country = "";

        try {
            JsonNode jsonNode = apiRequestBuilder(url);

            JsonNode forecastList = jsonNode.get("list");


            for (JsonNode forecast : forecastList) {
                WeatherDto weatherDto = new WeatherDto();

                long timestamp = forecast.get("dt").asLong();
                Date date = new Date(timestamp * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String dateTime = sdf.format(date);
                double temperature = forecast.get("main").get("temp").asDouble();
                String weatherDescription = forecast.get("weather").get(0).get("description").asText();
                country = jsonNode.get("city").get("country").asText();

                weatherDto.setCity(city.getCity());
                weatherDto.setCountry(country);
                weatherDto.setTemperature(temperature);
                weatherDto.setDate(dateTime);
                weatherDto.setWeatherDescription(weatherDescription);
                list.add(weatherDto);

            }
            UserRequest userRequest = new UserRequest(city.getCity(), country, LocalDateTime.now());
            requestRepository.save(userRequest);
            return list;

        } catch (Exception e) {
            if (city.getCity().isEmpty() || city.getCity().isBlank()) {
                throw new CityCouldNotNullException("City could not blank:");

            } else {
                throw new CityNotFoundException("City could not found : " + city.getCity());
            }


        }


    }


    public List<WeatherDto> getWeatherForecastDaily(RequestCity city) {
        String url = weatherUrlForecast + city.getCity() + "&units=metric&cnt=7&appid=" + API_KEY + "&lang=tr";

        List<WeatherDto> list = new ArrayList<>();
        String country = "";

        try {
            JsonNode jsonNode = apiRequestBuilder(url);
            JsonNode forecastList = jsonNode.get("list");

            for (JsonNode forecast : forecastList) {
                WeatherDto weatherDto = new WeatherDto();

                long timestamp = forecast.get("dt").asLong();
                Date date = new Date(timestamp * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String dateTime = sdf.format(date);
                double temperature = forecast.get("main").get("temp").asDouble();
                String weatherDescription = forecast.get("weather").get(0).get("description").asText();
                country = jsonNode.get("city").get("country").asText();

                weatherDto.setCity(city.getCity());
                weatherDto.setCountry(country);
                weatherDto.setTemperature(temperature);
                weatherDto.setDate(dateTime);
                weatherDto.setWeatherDescription(weatherDescription);

                list.add(weatherDto);
            }

            UserRequest userRequest = new UserRequest(city.getCity(), country, LocalDateTime.now());
            requestRepository.save(userRequest);
            return list;
        } catch (Exception e) {
            if (city.getCity().isEmpty() || city.getCity().isBlank()) {
                throw new CityCouldNotNullException("City could not be blank:");
            } else {
                throw new CityNotFoundException("City could not be found: " + city.getCity());
            }
        }
    }


}








