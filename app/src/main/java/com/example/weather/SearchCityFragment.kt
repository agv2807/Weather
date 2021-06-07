package com.example.weather

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var searchEt: EditText
    lateinit var backIv: ImageView
    var citiesString = mutableListOf<String>()
    var citiesStrAfterFilter = listOf<String>()

    lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_search_city, container, false)

        citiesTask().execute()

        citiesListRv = view.findViewById(R.id.citiesListRv)
        citiesListRv.layoutManager = LinearLayoutManager(activity)

        backIv = view.findViewById(R.id.backIv)
        backIv.setOnClickListener{
            (context as MainActivity).closeSearchMenu()
        }

        searchEt = view.findViewById(R.id.searchEt)
        searchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForCities(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return view
    }

    fun searchForCities(str: String){
        citiesStrAfterFilter = citiesString.filter { it.startsWith(str) }
        citiesListRv.adapter = adapter
        adapter.setDataForSearch(citiesStrAfterFilter)
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
                countriesList.forEach {
                    it.areas.forEach {
                        it.areas.forEach {
                            citiesString.add(it.name)
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