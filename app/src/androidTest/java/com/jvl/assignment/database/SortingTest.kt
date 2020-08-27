package com.jvl.assignment.database

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.utility.*
import org.junit.Assert.assertTrue

/**
 * Initialize the database and verify whether the repository can provide the viewmodel with data:
 * - Test went successful
 * - Database is filled with more then 1 entry of restaurants
 * - ..
 * @author Jaspervl
 */
@RunWith(AndroidJUnit4::class)
class SortingTest {
    private val activeMetric = MutableLiveData(Metric.BEST_MATCH)
    private val comparator = RestaurantComparator(activeMetric)
    private lateinit var restaurants: List<Restaurant>

    // Original dataset has 19 entries, for the testing I made a reduced one to test the order
    private val dummyDataSize = 9

    // Only testing three of the metrics
    private val orderMapping = mutableMapOf(
        Metric.BEST_MATCH to listOf<String>(
            "Lunchpakketdienst", "De Amsterdamsche Tram", "Tanoshii Sushi",
            "Fes Patisserie", "Feelfood", "Mama Mia",
            "Zenzai Sushi", "Pamukkale", "Tandoori Express"
        ),
        Metric.RATING_AVERAGE to listOf<String>(
            "Tanoshii Sushi", "Lunchpakketdienst", "De Amsterdamsche Tram",
            "Feelfood", "Mama Mia", "Fes Patisserie",
            "Tandoori Express", "Zenzai Sushi", "Pamukkale"
        ),
        Metric.MIN_COST to listOf<String>(
            "De Amsterdamsche Tram", "Tanoshii Sushi", "Lunchpakketdienst",
            "Mama Mia", "Fes Patisserie", "Feelfood",
            "Tandoori Express", "Zenzai Sushi", "Pamukkale"
        )
    )

    @Before
    fun loadDummyData() {
        restaurants = readData(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun correctSize() {
        assertTrue(
            "Expected size: $dummyDataSize, but was ${restaurants.size}",
            restaurants.size == dummyDataSize
        )
    }

    @Test
    fun correctOrder() {
        orderMapping.forEach { (metric, list) ->
            activeMetric.postValue(metric)
            val correctOrder = restaurants.sortedWith(comparator)
                .map { it.name }
                .filter { list.contains(it) }
                .reversed()
                .zip(list)
            val result = correctOrder.all { it.first == it.second }
            assertTrue("Sorting using $metric-> $correctOrder failed",result)
        }
    }

    private fun readData(context: Context): List<Restaurant> {
        JsonReader(context.assets.open(TEST_DATA_FILENAME).bufferedReader()).use { jsonReader ->
            val holder = Gson().fromJson<RestaurantHolder>(jsonReader, RestaurantHolder::class.java)
            return holder.restaurants
        }
    }

}