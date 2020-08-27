package com.jvl.assignment.utility

import com.jvl.assignment.model.entities.Restaurant

/**
 * Class representing the serialized form of the dummy data
 */
data class RestaurantHolder(
    val restaurants: List<Restaurant>
)