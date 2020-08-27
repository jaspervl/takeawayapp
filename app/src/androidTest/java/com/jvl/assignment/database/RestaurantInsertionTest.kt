package com.jvl.assignment.database

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jvl.assignment.model.RestaurantRepository
import com.jvl.assignment.util.RestaurantUtil
import com.jvl.assignment.util.getValueDelayed

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jvl.assignment.model.RestaurantDatabase
import com.jvl.assignment.model.dao.RestaurantDao
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.workers.RestaurantDatabaseWorker
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue

/**
 * Initialize the database and verify whether the repository can provide the viewmodel with data:
 * - Test went successful
 * - Database is filled with more then 1 entry of restaurants
 * - ..
 */
@RunWith(AndroidJUnit4::class)
class RestaurantInsertionTest {
    private lateinit var database: RestaurantDatabase
    private lateinit var restaurantDao: RestaurantDao

    private val restaurantCount = 20
    private var sortedDummyRestaurants = RestaurantUtil.generateRestaurants(restaurantCount)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDatabase() = runBlocking {
        // Load the dummy data into the database
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RestaurantDatabase::class.java)
            .build()
        restaurantDao = database.restaurantDao()
        restaurantDao.insertAll(sortedDummyRestaurants)

    }

    @After fun cleanup() = database.close()

    // Checks whether the worker populates the database successfully
    @Test
    fun testRetrieval() = runBlocking{
        val restaurants = restaurantDao.retrieveAll("").getValueDelayed(2)
        assertTrue("Retrieval failed, restaurant size was ${restaurants.size}",restaurants.size == restaurantCount)
    }

    @Test
    fun testFavorite() = runBlocking {
        val liveRestaurants = restaurantDao.retrieveAll("")
        val firstRestaurantBefore = liveRestaurants.getValueDelayed(2).first()
        assertTrue("Restaurant was already favorited", !firstRestaurantBefore.favorite)
        firstRestaurantBefore.favorite = true
        restaurantDao.update(firstRestaurantBefore)

        val firstRestaurantAfter = liveRestaurants.getValueDelayed(2).first()
        assertTrue("Live data didn't update restaurant", firstRestaurantAfter.favorite)
    }

}