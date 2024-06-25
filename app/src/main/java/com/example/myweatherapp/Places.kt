package com.example.myweatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.databinding.FragmentPlacesBinding
import data.CityDao
import data.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Places : Fragment(), CityAdapter.CityClickListener {
    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CityEntityAdapter
    private lateinit var cityDao: CityDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityDao = CityDatabase.getDatabase(requireContext()).cityDao()

        setupRecyclerView()
        loadCitiesFromDatabase()
    }

    private fun setupRecyclerView() {
        adapter = CityEntityAdapter(emptyList(), this)

        binding.recyclerFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@Places.adapter
        }
    }


    private fun loadCitiesFromDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            val cities = cityDao.getAllCities()
            Log.d("prueba", "Número de ciudades recuperadas: ${cities.size}")
            withContext(Dispatchers.Main) {
                adapter.updateCities(cities)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCityClicked(city: City) {
        // Implementa la lógica que desees al hacer clic en una ciudad
        // Por ejemplo, podrías abrir detalles de la ciudad o realizar alguna acción.
    }
}