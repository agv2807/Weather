package com.example.weather.home_screen.search_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class CitiesListAdapterRv: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var array = listOf<GetCitiesResponseElement>()
    lateinit var changeClouser: (GetCitiesResponseElement) -> Unit

    fun setData(cities: List<GetCitiesResponseElement>, clouser: (GetCitiesResponseElement) -> Unit) {
        array = cities
        changeClouser = clouser
        notifyDataSetChanged()
    }

    fun setDataForSearch(cities: List<GetCitiesResponseElement>) {
        array = cities
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = array.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.city, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderCustom = holder as ItemViewHolder
        holderCustom.bind(item = array.elementAt(position))

        holder.itemView.setOnClickListener {
            changeClouser(array.elementAt(position))
        }
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: GetCitiesResponseElement) {
            val cityName = itemView.findViewById<TextView>(R.id.city_name)
            cityName.text = item.name
        }
    }
}