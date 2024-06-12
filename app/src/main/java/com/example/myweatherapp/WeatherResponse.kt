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
    @Json(name = "temperature_2m") val temperature2m: String,
    @Json(name = "is_day") val isDay: String,
    val precipitation: String,
    val rain: String,
    @Json(name = "weather_code") val weatherCode: String,
    @Json(name = "precipitation_probability") val precipitationProbability: String
)

data class Current(
    val time: String,
    val interval: Int,
    @Json(name = "temperature_2m") val temperature2m: Double,
    @Json(name = "is_day") val isDay: Int,
    val precipitation: Double,
    val rain: Double,
    @Json(name = "weather_code") val weatherCode: Int
)

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
    @Json(name = "precipitation_probability") val precipitationProbability: List<Int>,
    val rain: List<Double>,
    @Json(name = "weather_code") val weatherCode: List<Int>
)

data class Daily(
    val time: List<String>,
    @Json(name = "weather_code") val weatherCode: List<Int>,
    @Json(name = "temperature_2m_max") val temperature2mMax: List<Double>,
    @Json(name = "temperature_2m_min") val temperature2mMin: List<Double>,
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: List<Int>
)
