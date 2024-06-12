package com.example.myweatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.ActivityMainBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        val temperatureList = listOf(
            Temperature("2024-06-11T00:00", 3.0),
            Temperature("2024-06-11T01:00", 6.8),
            Temperature("2024-06-11T02:00", 12.4),
            Temperature("2024-06-11T03:00", 14.2),
            Temperature("2024-06-11T03:00", 14.2),
            Temperature("2024-06-11T03:00", 14.2),
            Temperature("2024-06-11T03:00", 14.2),
            Temperature("2024-06-11T03:00", 14.2),
        )


        val formattedTemperatureList = temperatureList.map { temperature ->
            val dateTime = LocalTime.parse(temperature.hour, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val horaFormateada = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            Temperature(horaFormateada, temperature.degrees)
        }

        binding.recycler.adapter = TemperatureAdapter(formattedTemperatureList)



        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.places -> replaceFragment(Places())

                else -> {
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }
}