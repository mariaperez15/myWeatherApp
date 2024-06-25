package data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCityById(id: Int): CityEntity?

    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>

    @Query("DELETE FROM cities WHERE id = :id")
    suspend fun deleteCityById(id: Int)
}