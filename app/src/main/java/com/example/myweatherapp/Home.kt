package com.example.myweatherapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.FragmentHomeBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Home : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.adapter = TemperatureAdapter(formattedTemperatureList)
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
