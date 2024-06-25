package data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val currentTemperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val precipitation: Double,
    val updatedAt: String
)