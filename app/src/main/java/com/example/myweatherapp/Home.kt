package com.example.myweatherapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class Home : Fragment() {
    private lateinit var temperatureAdapter: TemperatureAdapter
    private lateinit var apiService: WeatherAPIService
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var updateHandler: Handler
    private var updateRunnable: Runnable? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var updateFrequency: Long = 900000 // Default to 15 minutes in milliseconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        temperatureAdapter = TemperatureAdapter(emptyList())
        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.adapter = temperatureAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(WeatherAPIService::class.java)

        dataStoreManager = DataStoreManager(requireContext())

        updateHandler = Handler(Looper.getMainLooper())

        lifecycleScope.launch {
            dataStoreManager.updateFrequency.collect { frequency ->
                updateFrequency = getUpdateFrequency(frequency)
            }
        }

        lifecycleScope.launch {
            val selectedCity = dataStoreManager.selectedCity.first() ?: "Madrid"
            val latitude = dataStoreManager.selectedCityLatitude.first() ?: 40.42
            val longitude = dataStoreManager.selectedCityLongitude.first() ?: -3.6999998
            startPeriodicUpdates(latitude, longitude, selectedCity)
        }

        binding.addButton.setOnClickListener {
            val intent = Intent(activity, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startPeriodicUpdates(latitude: Double, longitude: Double, cityName: String) {
        updateRunnable?.let {
            updateHandler.removeCallbacks(it)
        }
        updateRunnable = object : Runnable {
            override fun run() {
                fetchWeatherData(latitude, longitude, cityName)
                updateHandler.postDelayed(this, updateFrequency)
            }
        }
        updateHandler.post(updateRunnable!!)
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double, cityName: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = apiService.getWeather(
                    latitude = latitude,
                    longitude = longitude,
                    currentParams = "temperature_2m,is_day,precipitation,rain,weather_code",
                    hourlyParams = "temperature_2m,precipitation_probability,rain,weather_code",
                    dailyParams = "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max"
                )
                if (response.isSuccessful) {
                    val currentTime = Calendar.getInstance().apply {
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time
                    val forecastResponse = response.body()
                    forecastResponse?.let { response ->
                        val isDay = response.current.is_day == 1

                        applyDayNightBackground(isDay)

                        val temperatureList = response.hourly.time.zip(response.hourly.temperature_2m)
                            .mapNotNull { (time, temperature) ->
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                                val date = inputFormat.parse(time)
                                if (date != null && date >= currentTime) {
                                    Temperature(
                                        hour = time.substring(11, 16),
                                        degrees = temperature,
                                        precipitationProbability = response.hourly.precipitation_probability[response.hourly.time.indexOf(time)]
                                    )
                                } else {
                                    null
                                }
                            }
                        temperatureAdapter.updateData(temperatureList)

                        binding.rain.text = "${response.current.precipitation} mm"
                        binding.temperatureMax.text = "${response.daily.temperature_2m_max} °C"
                        binding.temperatureMin.text = "${response.daily.temperature_2m_min} °C"
                        binding.currentTemp.text = "${response.current.temperature_2m} °C"
                        binding.cityName.text = cityName

                        val maxTemperature = response.daily.temperature_2m_max.maxOrNull()
                        maxTemperature?.let {
                            binding.temperatureMax.text = "${it} °C"
                        }

                        val minTemperature = response.daily.temperature_2m_min.minOrNull()
                        minTemperature?.let {
                            binding.temperatureMin.text = "${it} °C"
                        }
                    }
                    updateCurrentTime()
                } else {
                    Log.e("HomeFragment", "Failed to fetch data for $cityName")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun applyDayNightBackground(isDay: Boolean) {
        val root = binding.root
        root.setBackgroundResource(if (isDay) R.drawable.bg_day else R.drawable.bg_night)
    }

    private fun updateCurrentTime() {
        val currentTime = Date()
        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime)
        binding.updateAt.text = "Updated at $formattedTime"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Cancel periodic updates when fragment is destroyed
        updateRunnable?.let {
            updateHandler.removeCallbacks(it)
        }
    }

    private fun getUpdateFrequency(frequency: String?): Long {
        val defaultFrequency = 900000 // Default to 15 minutes in milliseconds
        return when (frequency) {
            "1 minuto" -> 60000 // 1 minuto en milisegundos
            "2 minutos" -> 120000 // 2 minutos en milisegundos
            "3 minutos" -> 180000 // 3 minutos en milisegundos
            "15 minutos" -> 900000 // 15 minutos en milisegundos
            else -> defaultFrequency.toLong()
        }
    }
}
