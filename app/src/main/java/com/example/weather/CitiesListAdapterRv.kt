package com.example.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitiesListAdapterRv: RecyclerView.Adapter<CitiesListAdapterRv.ViewHolder>() {

    var array = mutableListOf<String>()
    lateinit var changeClouser: (String) -> Unit

    fun setData(cities: MutableList<String>, clouser: (String) -> Unit){
        array = cities
        changeClouser = clouser
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var cityName: TextView? = null

        init {
            cityName = itemView.findViewById(R.id.cityName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.city, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName?.text = array[position]

        holder.cityName?.setOnClickListener{
            changeClouser(array[position])
        }
    }

    override fun getItemCount(): Int = array.size

}