package com.example.myweatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Home : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var temperatureAdapter: TemperatureAdapter
    private lateinit var apiService: WeatherAPIService

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

        fetchWeatherData(40.4165, -3.7026, "Madrid")
        updateCurrentTime()
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double, cityName: String) {
        lifecycleScope.launch {
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
                        val temperatureList = response.hourly.time.zip(response.hourly.temperature_2m)
                            .mapNotNull { (time, temperature) ->
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                val date = inputFormat.parse(time)
                                val formattedTime = outputFormat.format(date)
                                val hour = outputFormat.calendar.get(Calendar.HOUR_OF_DAY)
                                val minute = outputFormat.calendar.get(Calendar.MINUTE)

                                if (date >= currentTime) {
                                    Temperature(hour = formattedTime, degrees = temperature)
                                } else {
                                    null
                                }
                            }

                        temperatureAdapter.updateData(temperatureList)
                        temperatureList.forEach { temperature ->
                            Log.d("prueba", "Hour: ${temperature.hour}, Degrees: ${temperature.degrees}")
                        }

                        val firstHourRainProbability = response.hourly.precipitation_probability.firstOrNull()
                        binding.rainProbability.text = "${firstHourRainProbability ?: "-"}%"

                        binding.cityName.text = cityName
                    }
                } else {
                    Log.e("prueba", "Failed to fetch data for $cityName")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun updateCurrentTime() {
        val currentTime = Date()
        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime)
        binding.updateAt.text = "Updated at $formattedTime"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}