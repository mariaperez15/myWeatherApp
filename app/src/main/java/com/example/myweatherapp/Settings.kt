package com.example.myweatherapp

import android.content.Context
import android.content.SharedPreferences
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
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        dataStoreManager = DataStoreManager(requireContext())
        updateFrequencySpinner = view.findViewById(R.id.updateFrequencySpinner)

        setupSpinner()

        return view
    }

    private fun setupSpinner() {
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
}
