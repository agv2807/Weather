package com.example.weather.home_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application): AndroidViewModel(application) {

    val weatherData: MutableLiveData<WeatherInfoResponse> by lazy {
        MutableLiveData<WeatherInfoResponse>()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun fetchWeather(weatherApi: WeatherApi?, lat: String?, lon: String?) {
        weatherApi?.let {
            compositeDisposable.add(it.getWeatherInfo(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    weatherData.value = it
                }, {

                }))
        }
    }
}