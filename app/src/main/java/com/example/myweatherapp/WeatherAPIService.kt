package com.example.myweatherapp
import WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherAPIService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentParams: String,
        @Query("hourly") hourlyParams: String,
        @Query("daily") dailyParams: String
    ): Response<WeatherResponse>
}


//@GET("/v1/forecast")
//suspend fun getForecast(
//    @Query("latitude") latitude: Double,
//    @Query("longitude") longitude: Double,
//    @Query("current") current: String,
//    @Query("daily") daily: String
//): ForecastResponse