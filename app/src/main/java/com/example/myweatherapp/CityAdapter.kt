package com.example.myweatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.CityItemBinding

class CityAdapter(
    private val cities: List<City>,
    private val clickListener: CityClickListener
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    interface CityClickListener {
        fun onCityClicked(city: City)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    inner class CityViewHolder(private val binding: CityItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(city: City) {
            binding.cityName.text = city.name
            binding.root.setOnClickListener {
                clickListener.onCityClicked(city)
            }
        }
    }
}
