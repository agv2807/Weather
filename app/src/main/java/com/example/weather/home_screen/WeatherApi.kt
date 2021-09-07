package com.example.weather.home_screen

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/onecall")
    @Headers("Content-Type: application/json")
    fun getWeatherInfo(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appId: String = "e77dcf9686c1d8ab75515118972ffc7c",
        @Query("exclude") exclude: String = "hourly,daily",
        @Query("units") units: String = "metric"
    ): Single<WeatherInfoResponse>
}