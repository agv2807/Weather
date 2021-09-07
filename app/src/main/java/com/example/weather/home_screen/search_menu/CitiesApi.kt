package com.example.weather.home_screen.search_menu

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface CitiesApi {

    @GET("areas")
    @Headers("Content-Type: application/json")
    fun getCityList(): Single<Array<GetCitiesResponseElement>>
}