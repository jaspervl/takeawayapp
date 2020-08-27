package com.jvl.assignment.database

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.util.RestaurantUtil
import com.jvl.assignment.utility.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThat
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
    private val orderMapping = mutableMapOf<Metric, List<String>>(
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

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun correctOrder() = runBlocking {
        orderMapping.forEach { (metric, list) ->
            activeMetric.value = metric
            val correctOrder = restaurants.sortedWith(comparator)
                .map { it.name }
                .filter { list.contains(it) }
                .reversed()
                .zip(list)
            val result = correctOrder.all { it.first == it.second }
            assertTrue("Sorting using $metric-> $correctOrder failed", result)
        }
    }

    private fun List<Restaurant>.sortByMetric(metric: Metric) = this.sortedWith(comparator.also {
        activeMetric.value = metric
    })

    // FIXME Note this test doesn't really work since the generated restaurants aren't ordered alphabetically due to the UUID used
    //
    //    @Test
    //    fun sortRandomAscendingElements() = runBlocking {
    //        val sortedRestaurants = RestaurantUtil.generateRestaurants(200)
    //        val shuffled =
    //            sortedRestaurants.shuffled() // Create a new list and randomize the element order
    //
    //        val metricMap = mapOf(
    //            Metric.BEST_MATCH to shuffled.sortByMetric(Metric.BEST_MATCH).reversed(),
    //            Metric.NEWEST to shuffled.sortByMetric(Metric.NEWEST),
    //            Metric.RATING_AVERAGE to shuffled.sortByMetric(Metric.RATING_AVERAGE),
    //            Metric.DISTANCE to shuffled.sortByMetric(Metric.DISTANCE),
    //            Metric.POPULARITY to shuffled.sortByMetric(Metric.POPULARITY),
    //            Metric.AVERAGE_PRODUCT_PRICE to shuffled.sortByMetric(Metric.AVERAGE_PRODUCT_PRICE),
    //            Metric.DELIVERY_COST to shuffled.sortByMetric(Metric.DELIVERY_COST),
    //            Metric.MIN_COST to shuffled.sortByMetric(Metric.MIN_COST)
    //        )
    //
    //        // Convert it to only the names for easier mapping
    //        val sortedRestaurantNames = sortedRestaurants.map { it.name }
    //        metricMap
    //            .mapValues { pair -> pair.value.map { it.name } }
    //            .forEach { (metric, list) ->
    //            assertTrue("List wasn't the same for $metric -> ${list.zip(sortedRestaurantNames)}", list == sortedRestaurantNames)
    //        }
    //
    //    }


    // Helper method to read the data
    private fun readData(context: Context): List<Restaurant> {
        JsonReader(context.assets.open(TEST_DATA_FILENAME).bufferedReader()).use { jsonReader ->
            val holder = Gson().fromJson<RestaurantHolder>(jsonReader, RestaurantHolder::class.java)
            return holder.restaurants
        }
    }

}