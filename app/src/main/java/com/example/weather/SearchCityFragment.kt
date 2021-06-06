package com.example.weather

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class SearchCityFragment : Fragment() {

    lateinit var citiesListRv: RecyclerView
    val adapter = CitiesListAdapterRv()

    lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_search_city, container, false)

        citiesTask().execute()

        citiesListRv = view.findViewById(R.id.citiesListRv)
        citiesListRv.layoutManager = LinearLayoutManager(activity)


        return view
    }


    inner class citiesTask(): AsyncTask<String, Void, String>(){

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.hh.ru/areas").readText(Charsets.UTF_8)
            }
            catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val gson = Gson()
                var countriesList: List<Country> = gson.fromJson(result, Array<Country>::class.java).toList()
                var citiesString = mutableListOf<String>()
                countriesList.forEach {
                    it.areas.forEach {
                        it.areas.forEach {
                            citiesString?.add(it.name)
                        }
                    }
                }

                citiesListRv.adapter = adapter
                adapter.setData(citiesString){
                    viewModel.city.value = it
                }
                

            }
            catch (e: Exception){
                Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
data class City(val name: String)

data class Area(val name: String, val areas: List<City>)

data class Country(val name: String, val areas: List<Area>)