package com.example.myweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.TemperatureItemBinding

class TemperatureAdapter(private val temperature: List<Temperature>) :
    RecyclerView.Adapter<TemperatureAdapter.ViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): TemperatureAdapter.ViewHolder {
        val binding =TemperatureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false

        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TemperatureAdapter.ViewHolder, position: Int) {
        holder.bind(temperature[position])
    }

    override fun getItemCount() = temperature.size

    class ViewHolder(private val binding: TemperatureItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(temperature: Temperature){
            binding.hour.text = temperature.hour
            binding.degrees.text = temperature.degrees.toString()
        }
    }
}