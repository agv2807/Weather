package com.example.weather.home_screen

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.example.weather.R
import com.example.weather.home_screen.search_menu.GeoCodeViewModel
import com.example.weather.home_screen.search_menu.SearchCityFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var prefLon: SharedPreferences
    private lateinit var prefLat: SharedPreferences
    private lateinit var prefCity: SharedPreferences

    private var defaultLon: String? = "94.45"
    private var defaultLat: String? = "51.75"

    private val searchFragment = SearchCityFragment()

    private lateinit var toggle: ActionBarDrawerToggle

    private var address: TextView? = null
    private var addressCont: LinearLayout? = null
    private var updatedAt: TextView? = null
    private var weatherStatus: TextView? = null
    private var temp: TextView? = null
    private var feelTemp: TextView? = null
    private var sunrise: TextView? = null
    private var sunset: TextView? = null
    private var windSpeed: TextView? = null
    private var pressure: TextView? = null
    private var humidity: TextView? = null
    private var loader: ProgressBar? = null
    private var errorText: TextView? = null
    private var mainCont: RelativeLayout? = null
    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupViews()

        setupSettings()

        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        if (prefLat.contains("lat") || prefLon.contains("lon") || prefCity.contains("city")) {
            getSettings()
        }

        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.fetchWeather(weatherApi = (application as? WeatherApp)?.weatherApi, defaultLat, defaultLon)
        }

        homeViewModel.weatherData.observe(this, {
            setWeatherInfo(it)
            loadOff()
        })

        val geoCodeViewModel = GeoCodeViewModel.instance
        geoCodeViewModel.cityCoords.observe(this, {

            address?.text = it.name
            closeSearchMenu()

            CoroutineScope(Dispatchers.IO).launch {
                homeViewModel.fetchWeather(weatherApi = (application as? WeatherApp)?.weatherApi,
                    it.lat, it.lon)
                saveCoords(lat = it.lat, lon = it.lon, city = address?.text.toString())
            }
        })
    }

    private fun getSettings() {
        defaultLat = prefLat.getString("lat", "51.75")
        defaultLon = prefLon.getString("lon", "94.45")
        address?.text = prefCity.getString("city", "City")
    }

    private fun setupSettings() {
        prefLat = getSharedPreferences("settings_lat", Context.MODE_PRIVATE)
        prefLon = getSharedPreferences("settings_lon", Context.MODE_PRIVATE)
        prefCity = getSharedPreferences("settings_city", Context.MODE_PRIVATE)
    }

    private fun saveCoords(lat: String, lon: String, city: String) {
        var editor = prefLat.edit()
        editor.putString("lat", lat).apply()
        editor = prefLon.edit()
        editor.putString("lon", lon).apply()
        editor = prefCity.edit()
        editor.putString("city",city).apply()
    }

    private fun loadOn() {
        loader?.visibility = View.VISIBLE
        mainCont?.visibility = View.GONE
    }

    private fun loadOff() {
        loader?.visibility = View.GONE
        mainCont?.visibility = View.VISIBLE
    }

    private fun setWeatherInfo(response: WeatherInfoResponse?) {
        response?.let {
            updatedAt?.text = SimpleDateFormat("HH:mm dd MMMM yyyy", Locale.ENGLISH).format(
                Date(it.current.dt * 1000)
            )
            weatherStatus?.text = it.current.weather.first().main
            temp?.text = "${it.current.temp.toInt()} °C"
            feelTemp?.text = "Feels like ${it.current.feelsLike.toInt()} °C"
            sunrise?.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(
                Date(it.current.sunrise * 1000)
            )
            sunset?.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(
                Date(it.current.sunset * 1000)
            )
            windSpeed?.text = it.current.windSpeed.toString()
            pressure?.text = it.current.pressure.toString()
            humidity?.text = it.current.humidity.toString()
        }
    }

    private fun setupViews() {
        address = findViewById(R.id.address)
        updatedAt = findViewById(R.id.updated_at)
        weatherStatus = findViewById(R.id.status)
        temp = findViewById(R.id.temp)
        feelTemp = findViewById(R.id.temp_feel)
        sunrise = findViewById(R.id.sunrise)
        sunset = findViewById(R.id.sunset)
        windSpeed = findViewById(R.id.wind)
        pressure = findViewById(R.id.pressure)
        humidity = findViewById(R.id.humidity)
        loader = findViewById(R.id.loader)
        errorText = findViewById(R.id.errortext)
        mainCont = findViewById(R.id.mainContainer)
        addressCont = findViewById(R.id.adress_container)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        address?.setOnClickListener {
            openSearchMenu()
        }
        loadOn()

        setupNavigationDrawer()
    }

    private fun setupNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView?.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> Toast.makeText(this, "Clicked Home", Toast.LENGTH_SHORT).show()
                R.id.nav_delete -> Toast.makeText(this, "Clicked Delete", Toast.LENGTH_SHORT).show()
                R.id.nav_settings -> Toast.makeText(this, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_sync -> Toast.makeText(this, "Clicked Sync", Toast.LENGTH_SHORT).show()
                R.id.nav_share -> Toast.makeText(this, "Clicked Share", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count != 0) {
            closeSearchMenu()
        } else {
            super.onBackPressed()
        }
    }

    private fun openSearchMenu() {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.searchFragment, searchFragment)
            commit()
            addressCont?.visibility = View.GONE
        }
    }

    fun closeSearchMenu() {
        searchFragment.removeRecycler()

        supportFragmentManager.beginTransaction().apply {
            remove(searchFragment)
            commit()
        }

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        addressCont?.visibility = View.VISIBLE
    }
}

