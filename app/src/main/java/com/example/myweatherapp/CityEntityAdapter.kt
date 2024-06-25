package com.example.myweatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.CityItemBinding
import com.example.myweatherapp.databinding.FavItemBinding
import data.CityEntity

class CityEntityAdapter(
    private var cities: List<CityEntity>,
    private val clickListener: CityAdapter.CityClickListener
) : RecyclerView.Adapter<CityEntityAdapter.CityEntityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityEntityViewHolder {
        val binding = FavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityEntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityEntityViewHolder, position: Int) {
        val cityEntity = cities[position]
        holder.bind(cityEntity)
        Log.d("CityEntityAdapter", "Binding city: ${cityEntity.name}")
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun updateCities(newCities: List<CityEntity>) {
        cities = newCities
        notifyDataSetChanged()
    }

    inner class CityEntityViewHolder(private val binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cityEntity: CityEntity) {
            binding.favCityName.text = cityEntity.name
            binding.root.setOnClickListener {
                val city = City(
                    cityEntity.name,
                    cityEntity.latitude,
                    cityEntity.longitude
                )
                clickListener.onCityClicked(city)
            }
        }
    }
}