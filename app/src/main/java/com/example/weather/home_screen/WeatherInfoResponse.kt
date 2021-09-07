package com.example.weather.home_screen

import com.google.gson.annotations.SerializedName

data class WeatherInfoResponse (
    val lat: Double,
    val lon: Double,
    val timezone: String,

    @SerializedName("timezone_offset")
    val timezoneOffset: Long,

    val current: Current,
    val hourly: List<Current>
)

data class Current (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    val pressure: Long,
    val humidity: Long,

    @SerializedName("dew_point")
    val dewPoint: Double,

    val clouds: Long,
    val visibility: Long? = null,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    @SerializedName("wind_deg")
    val windDeg: Long,

    val weather: List<Weather>,

    @SerializedName("wind_gust")
    val windGust: Double? = null
)

data class Weather (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)
