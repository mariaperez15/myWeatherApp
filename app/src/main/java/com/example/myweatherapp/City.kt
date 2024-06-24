package com.example.myweatherapp

import java.io.Serializable

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double
) : Serializable