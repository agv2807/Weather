package com.example.weather.home_screen.search_menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GeoCodeViewModel: ViewModel() {

    companion object {
        val instance = GeoCodeViewModel()
    }

    init {
        configureRetrofit()
    }

    val cityCoords: MutableLiveData<CityCoords> by lazy {
        MutableLiveData<CityCoords>()
    }

    private lateinit var geoCodeApi: GeoCodeApi

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun fetchCoords(city: String?) {
        compositeDisposable.add(
            geoCodeApi.getCoords(q = city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cityCoords.value = it.first()
                }, {
                    Log.d("tag", it.message.toString())
                })
        )
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        geoCodeApi = retrofit.create(GeoCodeApi::class.java)
    }

}