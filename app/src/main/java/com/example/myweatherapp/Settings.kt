package com.example.myweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Settings : Fragment() {

    private lateinit var updateFrequencySpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        dataStoreManager = DataStoreManager(requireContext())
        updateFrequencySpinner = view.findViewById(R.id.updateFrequencySpinner)
        citySpinner = view.findViewById(R.id.citySpinner)

        setupSpinners()

        return view
    }

    private fun setupSpinners() {
        setupUpdateFrequencySpinner()
        setupCitySpinner()
    }

    private fun setupUpdateFrequencySpinner() {
        val options = resources.getStringArray(R.array.update_frequency_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        updateFrequencySpinner.adapter = adapter

        lifecycleScope.launch {
            dataStoreManager.updateFrequency.collect { currentFrequency ->
                val position = options.indexOf(currentFrequency)
                if (position >= 0) {
                    updateFrequencySpinner.setSelection(position)
                }
            }
        }

        updateFrequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFrequency = options[position]
                lifecycleScope.launch {
                    dataStoreManager.setUpdateFrequency(selectedFrequency)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Not implemented
            }
        }
    }

    private fun setupCitySpinner() {
        val cityNames = resources.getStringArray(R.array.city_names)
        val cityCoordinates = resources.getStringArray(R.array.city_coordinates)
        val cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityNames)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = cityAdapter

        lifecycleScope.launch {
            dataStoreManager.selectedCity.collect { selectedCity ->
                val position = cityNames.indexOf(selectedCity)
                if (position >= 0) {
                    citySpinner.setSelection(position)
                }
            }
        }

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = cityNames[position]
                val selectedCoordinates = cityCoordinates[position].split(",").map { it.toDouble() }
                lifecycleScope.launch {
                    dataStoreManager.setSelectedCity(selectedCity)
                    dataStoreManager.setSelectedCityCoordinates(selectedCoordinates[0], selectedCoordinates[1])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Not implemented
            }
        }
    }
}
