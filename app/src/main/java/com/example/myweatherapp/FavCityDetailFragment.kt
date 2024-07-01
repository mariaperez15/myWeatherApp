package com.example.myweatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
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
    private var selectedCityEntity: CityEntity? = null

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
                selectedCityEntity = withContext(Dispatchers.IO) {
                    cityDao.getCityByName(city.name)
                }
                selectedCityEntity?.let {
                    displayCityDetails(it)
                }
            }
        }

        binding.heartIcon.setOnClickListener {
            selectedCityEntity?.let { cityEntity ->
                lifecycleScope.launch(Dispatchers.IO) {
                    cityDao.deleteCity(cityEntity)
                }
                requireActivity().supportFragmentManager.popBackStack()
                setFragmentResult("cityDeleted", Bundle().apply {
                    putInt("deletedCityId", cityEntity.id)
                })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayCityDetails(cityEntity: CityEntity) {
        binding.FavCityNameDetails.text = cityEntity.name
        binding.favCurrentTemp.text = "${cityEntity.currentTemperature} °C"
        binding.favTemperatureMin.text = "${cityEntity.minTemperature} °C"
        binding.favTemperatureMax.text = "${cityEntity.maxTemperature} °C"
        binding.favRainText.text = "${cityEntity.precipitation} mm"
        binding.favUpdateAt.text = cityEntity.updatedAt
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
