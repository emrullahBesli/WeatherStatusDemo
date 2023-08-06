package com.weather.api.weatherStatus.dto

import javax.validation.constraints.NotEmpty

data class RequestCity(
        @field:NotEmpty(message = "Bir şehir adı yazın")
        val city: String="istanbul"
)

