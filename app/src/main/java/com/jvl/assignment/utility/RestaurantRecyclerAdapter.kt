package com.jvl.assignment.utility

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jvl.assignment.databinding.FragmentRestaurantItemBinding

import com.jvl.assignment.model.entities.Restaurant

/**
 * RecyclerView (List) adapter which displays the restaurant items
 * @author Jaspervl
 */
class RestaurantRecyclerAdapter constructor() :
    ListAdapter<Restaurant, RestaurantRecyclerAdapter.RestaurantViewHolder>(
        callback
    ) {

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) = holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RestaurantViewHolder(
        FragmentRestaurantItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    inner class RestaurantViewHolder(private val binding: FragmentRestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Restaurant) {
            binding.restaurant = item
            binding.executePendingBindings()
        }
    }

    /**
     * DiffUtil callback implementation necessary for the listadapter
     * The only time an item state is changed is when the favorite is altered by the user
     */
    companion object {
        val callback = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.favorite == newItem.favorite
            }

        }
    }
}