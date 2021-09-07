package com.example.weather.home_screen.search_menu

import com.google.gson.annotations.SerializedName

data class GetCitiesResponseElement (
    val id: String? = null,

    @SerializedName("parent_id")
    val parentID: String? = null,

    val name: String? = null,
    val areas: List<GetCitiesResponseElement>? = null
)