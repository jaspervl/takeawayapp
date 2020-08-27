package com.jvl.assignment.model.entities

/**
 * Sorting values belonging to a restaurant based on the provided example json data
 */
data class SortingValues(
    val bestMatch: Float,
    val newest: Float,
    val ratingAverage: Float,
    val distance: Int,
    val popularity: Float,
    val averageProductPrice: Int,
    val deliveryCosts: Int,
    val minCost: Int
)