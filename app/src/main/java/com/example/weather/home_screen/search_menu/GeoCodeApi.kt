package com.example.weather.home_screen.search_menu

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodeApi {

    @GET("/geo/1.0/direct?")
    fun getCoords(
        @Query("q") q: String?,
        @Query("limit") limit: String = "5",
        @Query("appid") appid: String = "e77dcf9686c1d8ab75515118972ffc7c"
    ): Single<List<CityCoords>>
}