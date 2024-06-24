import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myweatherapp.City
import com.example.myweatherapp.R
import com.example.myweatherapp.Temperature
import com.example.myweatherapp.TemperatureAdapter
import com.example.myweatherapp.WeatherAPIService
import com.example.myweatherapp.databinding.CityDetailsFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class CityDetailFragment : Fragment() {
    private var _binding: CityDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiService: WeatherAPIService
    private lateinit var temperatureAdapter: TemperatureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CityDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(WeatherAPIService::class.java)

        temperatureAdapter = TemperatureAdapter(emptyList())
        binding.recycler2.adapter = temperatureAdapter

        // Recuperar la ciudad seleccionada del Bundle
        val selectedCity = arguments?.getSerializable("selectedCity") as? City
        selectedCity?.let {
            fetchWeatherData(it.latitude, it.longitude)
            binding.cityName2.text = it.name // Actualizar el nombre de la ciudad en la interfaz de usuario
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
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
                    forecastResponse?.let { weatherResponse ->
                        val isDay = weatherResponse.current.is_day == 1

                        applyDayNightBackground(isDay)

                        // Actualizar vistas en el hilo principal
                        launch(Dispatchers.Main) {
                            val temperatureList = weatherResponse.hourly.time.zip(weatherResponse.hourly.temperature_2m)
                                .mapNotNull { (time, temperature) ->
                                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                                    val date = inputFormat.parse(time)
                                    if (date != null && date >= currentTime) {
                                        Temperature(
                                            hour = time.substring(11, 16),
                                            degrees = temperature,
                                            precipitationProbability = weatherResponse.hourly.precipitation_probability[weatherResponse.hourly.time.indexOf(time)]
                                        )
                                    } else {
                                        null
                                    }
                                }
                            temperatureAdapter.updateData(temperatureList)

                            binding.rain2.text = "${weatherResponse.current.precipitation} mm"
                            binding.temperatureMax2.text = "${weatherResponse.daily.temperature_2m_max} °C"
                            binding.temperatureMin2.text = "${weatherResponse.daily.temperature_2m_min} °C"
                            binding.currentTemp2.text = "${weatherResponse.current.temperature_2m} °C"

                            val maxTemperature = weatherResponse.daily.temperature_2m_max.maxOrNull()
                            maxTemperature?.let {
                                binding.temperatureMax2.text = "${it} °C"
                            }

                            val minTemperature = weatherResponse.daily.temperature_2m_min.minOrNull()
                            minTemperature?.let {
                                binding.temperatureMin2.text = "${it} °C"
                            }
                        }
                    }
                } else {
                    Log.e("CityDetailFragment", "Failed to fetch data")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CityDetailFragment", "Exception: ${e.message}")
            }
        }
    }

    private fun applyDayNightBackground(isDay: Boolean) {
        val mainContainer = binding.mainContainer
        mainContainer.setBackgroundResource(if (isDay) R.drawable.bg_day_night else R.drawable.bg_day_night)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
