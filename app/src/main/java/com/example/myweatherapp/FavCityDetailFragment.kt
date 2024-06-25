package com.example.myweatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myweatherapp.databinding.FavCityDetailsFragmentBinding
import data.CityDao
import data.CityDatabase
import data.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavCityDetailFragment : Fragment() {
    private var _binding: FavCityDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var cityDao: CityDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavCityDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityDao = CityDatabase.getDatabase(requireContext()).cityDao()

        val selectedCity = arguments?.getSerializable("selectedCity") as? City
        selectedCity?.let { city ->
            lifecycleScope.launch {
                val cityEntity = withContext(Dispatchers.IO) {
                    cityDao.getCityByName(city.name)
                }
                cityEntity?.let {
                    displayCityDetails(it)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayCityDetails(cityEntity: CityEntity) {
        binding.FavCityNameDetails.text = cityEntity.name
        binding.favCurrentTemp.text = "${cityEntity.currentTemperature} °C"
        binding.favTempMin.text = "${cityEntity.minTemperature} °C"
        binding.favTempMax.text = "${cityEntity.maxTemperature} °C"
        binding.favRainText.text = "${cityEntity.precipitation} mm"
        binding.favUpdateAt.text = "Updated at ${cityEntity.updatedAt}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
