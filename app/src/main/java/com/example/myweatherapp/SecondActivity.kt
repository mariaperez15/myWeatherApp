package com.example.myweatherapp

import CityDetailFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.databinding.ActivitySecondBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondActivity : AppCompatActivity(), CityAdapter.CityClickListener {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var cityAdapter: CityAdapter
    private var cities: List<City> = ArrayList()
    private lateinit var apiService: WeatherAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ciudades"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cities = fetchCities()

        binding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cityAdapter = CityAdapter(cities, this)
        binding.recycler.adapter = cityAdapter


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(WeatherAPIService::class.java)
    }

    override fun onCityClicked(city: City) {
        val fragment = CityDetailFragment()
        val bundle = Bundle().apply {
            putSerializable("selectedCity", city)
        }
        fragment.arguments = bundle
        supportActionBar?.title = city.name

        supportFragmentManager.commit {
            replace(R.id.detailsCity, fragment)
            addToBackStack(null)
        }
    }

    private fun fetchCities(): List<City> {
        return listOf(
            City("Burgos", 42.3411, -3.7018),
            City("Barcelona", 41.3888, 2.159),
            City("Sevilla", 37.3828, -5.9732),
            City("Leon", 42.6, -5.5703),
            City("Valladolid", 41.6552, -4.7237),
            City("Pamplona", 42.8169, -1.6432),
            City("Almería", 36.8381, -2.4597),
            City("Málaga", 36.7202, -4.4203),
            City("Córdoba", 37.8916, -4.7728),
            City("Huelva", 37.2664, -6.94),
            City("Nueva York", 40.7143, -74.006),
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            supportActionBar?.title = "Ciudades"
        } else {
            super.onBackPressed()
        }
    }

}
