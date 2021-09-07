package com.example.weather.home_screen.search_menu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.weather.R
import com.example.weather.home_screen.MainActivity

class SearchCityFragment: Fragment() {

    private var searchEt: EditText? = null
    private var backIv: ImageView? = null

    private val recyclerFragment = RecyclerFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_city, container, false)

        setupViews(view = view)

        parentFragmentManager.beginTransaction().apply {
            add(R.id.cities_list_container, recyclerFragment)
            commit()
        }

        return view
    }

    fun removeRecycler() {
        parentFragmentManager.beginTransaction().apply {
            remove(recyclerFragment)
            commit()
        }
    }

    private fun setupViews(view: View) {
        searchEt = view.findViewById(R.id.searchEt)
        backIv = view.findViewById(R.id.backIv)

        backIv?.setOnClickListener{
            removeRecycler()
            (context as MainActivity).closeSearchMenu()
        }

        searchEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
}
