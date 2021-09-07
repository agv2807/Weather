package com.example.weather.home_screen.search_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class RecyclerFragment : Fragment() {

    private var citiesListRv: RecyclerView? = null
    private var backIc: ImageView? = null
    private var loader: ProgressBar? = null

    private val adapter = CitiesListAdapterRv()

    var list = listOf<GetCitiesResponseElement>()

    var items = listOf<GetCitiesResponseElement>()
        set(value) {
            field = value
            loadOff()
            adapter.setData(cities = value) { item ->
                tapCloser?.let { it(item) }
            }
        }

    private var tapCloser: ((GetCitiesResponseElement) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_fragment, container, false)

        setupViews(view = view)

        val citiesListViewModel = ViewModelProviders.of(this).get(CitiesViewModel::class.java)

        citiesListViewModel.citiesList.observe(requireActivity(), {
            items = it
            list = it
        })

        citiesListViewModel.fetchCityList()

        val geoCodeViewModel = GeoCodeViewModel.instance

        citiesListRv?.layoutManager = LinearLayoutManager(activity)
        citiesListRv?.adapter = adapter

        tapCloser = {
            it.areas?.let { it1 ->
                if (it1.isEmpty()) {
                   geoCodeViewModel.fetchCoords(city = it.name)
                } else {
                    items = it1
                }
            }
        }

        return view
    }

    private fun setupViews(view: View) {
        citiesListRv = view.findViewById(R.id.cities_list)
        backIc = view.findViewById(R.id.back_ic)
        loader = view.findViewById(R.id.loader)

        backIc?.setOnClickListener {
            returnToPreviousList()
        }
        loadOn()
    }

    private fun loadOn() {
        loader?.visibility = View.VISIBLE
        citiesListRv?.visibility = View.GONE
    }

    private fun loadOff() {
        loader?.visibility = View.GONE
        citiesListRv?.visibility = View.VISIBLE
    }

    private fun returnToPreviousList() {
        items = searchParent(areas = list) ?: list
    }

    private fun searchParent(areas: List<GetCitiesResponseElement>): List<GetCitiesResponseElement>? {
        val parentId = items.first().parentID
        var parentAreas: List<GetCitiesResponseElement>? = null
        val emptyList = listOf<GetCitiesResponseElement>()
        areas.forEach {
            if (it.id == parentId) {
                parentAreas = if (parentAreas == null) areas else parentAreas
            }
        }
        if (parentAreas == null) {
            areas.forEach {
                parentAreas = if (parentAreas == null) searchParent(
                    areas = it.areas ?: emptyList
                ) else parentAreas
            }
        }
        return parentAreas
    }
}