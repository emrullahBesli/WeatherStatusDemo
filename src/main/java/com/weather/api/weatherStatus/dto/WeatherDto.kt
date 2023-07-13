package com.weather.api.weatherStatus.dto

data class WeatherDto (
        var city: String?,
        var country: String?,
        var temperature: Double?,
        var date: String?,
        var weatherDescription: String?


) {
    constructor() : this("","",0.0,"","") {

    }
}


