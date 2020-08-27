package com.jvl.assignment.utility

import androidx.lifecycle.LiveData
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.model.entities.SortingValues

/**
 * Part of the assignment is facilitating the sorting of with the following levels of priority:
 * - Favorite
 * - Opening state (OPEN,ORDER,CLOSED)
 * - Sorting options
 * @author Jaspervl
 */
class RestaurantComparator(private val activeMetric: LiveData<Metric>) : Comparator<Restaurant> {

    override fun compare(res1: Restaurant, res2: Restaurant) =
        // First check the favorites
        checkFavorites(res1, res2).also { favoriteResult ->
            // If
            return if (favoriteResult != 0) favoriteResult else checkStates(
                statusMapping[res1.status]!!,
                statusMapping[res2.status]!!
            ).also { statusResult ->
                return if (statusResult != 0) statusResult else checkMetric(
                    res1.sortingValues,
                    res2.sortingValues
                ).also { metricResult ->
                    return if (metricResult != 0) metricResult else checkAlphabetically(
                        res1.name,
                        res2.name
                    )
                }
            }
        }


    // Check the favorites
    private fun checkFavorites(res1: Restaurant, res2: Restaurant) = when {
        res1.favorite == res2.favorite -> 0
        res1.favorite -> 1
        else -> -1
    }

    // Check the possible states
    private fun checkStates(status1: Int, status2: Int) = when {
        status1 == status2 -> 0
        status1 > status2 -> 1
        else -> -1
    }

    // FIXME Seems a bit lengthy and a lot of repetition, should rethink this
    // Certain values are inverted to their negative for the comparison to make sense (min values)
    private fun checkMetric(val1: SortingValues, val2: SortingValues) = when (activeMetric.value) {
        Metric.BEST_MATCH -> val1.bestMatch.compareTo(val2.bestMatch)
        Metric.NEWEST -> val1.newest.compareTo(val2.newest) * -1
        Metric.RATING_AVERAGE -> val1.ratingAverage.compareTo(val2.ratingAverage)
        Metric.DISTANCE -> val1.distance.compareTo(val2.distance) * -1
        Metric.POPULARITY -> val1.popularity.compareTo(val2.popularity)
        Metric.AVERAGE_PRODUCT_PRICE -> val1.averageProductPrice.compareTo(val2.averageProductPrice) * -1
        Metric.DELIVERY_COST -> val1.deliveryCosts.compareTo(val2.deliveryCosts) * -1
        Metric.MIN_COST -> val1.minCost.compareTo(val2.minCost) * -1
        else -> 0 // No metric active
    }

    // Necessary to keep order consistent
    private fun checkAlphabetically(name1: String, name2: String) = name1.compareTo(name2)

    companion object {
        // Mapping of status, giving a higher value to the open value
        private val statusMapping = hashMapOf("open" to 3, "order ahead" to 2, "closed" to 1)
    }
}