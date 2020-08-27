package com.jvl.assignment.util

import androidx.annotation.FloatRange
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.model.entities.SortingValues
import java.util.*
import kotlin.random.Random as ran

/**
 * Util to generate dummy restaurants using UUID class in order to avoid duplicates
 */
class RestaurantUtil {

    companion object {
        private val statusOptions = setOf("open", "order ahead", "closed")

        // helper method to generate multiple restaurants
        fun generateRestaurants(number: Int) = mutableListOf<Restaurant>().apply {
            for (i in 0 until number) {
                this.add(generateRestaurant(i))
            }
        }

        private fun generateRestaurant(sortingValue: Int = -1): Restaurant {
            return Restaurant(
                name = UUID.randomUUID().toString(),
                status = statusOptions.random(),
                sortingValues = if(sortingValue != -1) generateRandomSortingValues() else generateSortingValues(sortingValue)
            )
        }

        private fun generateSortingValues(value: Int) = SortingValues(
            bestMatch = value.toFloat(),
            newest = value.toFloat(),
            ratingAverage = value.toFloat(),
            distance = value,
            popularity = value.toFloat(),
            averageProductPrice = value,
            deliveryCosts = value,
            minCost = value
        )

        private fun generateRandomSortingValues(): SortingValues {
            return SortingValues(
                bestMatch = randomFloat(500),
                newest = randomFloat(300),
                ratingAverage = randomFloat(50) / 10,
                distance = ran.nextInt(3000),
                popularity = randomFloat(50),
                averageProductPrice = ran.nextInt(1500),
                deliveryCosts = ran.nextInt(500),
                minCost = ran.nextInt(3000)
            )
        }

        private fun randomFloat(range: Int) = ran.nextFloat() * range
    }
}