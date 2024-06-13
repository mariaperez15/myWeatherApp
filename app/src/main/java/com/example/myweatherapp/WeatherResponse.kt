import com.squareup.moshi.Json

data class WeatherResponse(
    var latitude: Double,
    var longitude: Double,
    var generationtime_ms: Double,
    var utc_offset_seconds: Int,
    var timezone: String,
    var timezone_abbreviation: String,
    var elevation: Int,
    var current_units: Units,
    var current: Current,
    var hourly_units: Units,
    var hourly: Hourly,
    var daily_units: Units,
    var daily: Daily
)

data class Units(
    val time: String,
    val interval: String,
    val temperature_2m: String,
    val is_day: String,
    val precipitation: String,
    val rain: String,
    val weather_code: String,
    val precipitation_probability: String
)

data class Current(
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val is_day: Int,
    val precipitation: Double,
    val rain: Double,
    val weather_code: Int
)

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val precipitation_probability: List<Int>,
    val rain: List<Double>,
    val weather_code: List<Int>
)

data class Daily(
    val time: List<String>,
    val weather_code: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val precipitation_probability_max: List<Int>
)
