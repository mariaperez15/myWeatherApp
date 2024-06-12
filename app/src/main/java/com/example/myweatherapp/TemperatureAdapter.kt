package com.example.myweatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.TemperatureItemBinding

class TemperatureAdapter(private var temperatures: List<Temperature>) :
    RecyclerView.Adapter<TemperatureAdapter.ViewHolder>() {

    fun updateData(newTemperatures: List<Temperature>) {
        temperatures = newTemperatures
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TemperatureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(temperatures[position])
    }

    override fun getItemCount() = temperatures.size

    class ViewHolder(private val binding: TemperatureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(temperature: Temperature) {
            binding.hour.text = buildString {
                append(temperature.hour)
                append(" h.")
            }
            binding.degrees.text = buildString {
                append(temperature.degrees.toString())
                append(" º")
            }
        }
    }
}
