package com.jvl.assignment.viewmodel

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
import androidx.test.platform.app.InstrumentationRegistry
import com.jvl.assignment.model.RestaurantDatabase
import com.jvl.assignment.model.dao.RestaurantDao
import com.jvl.assignment.model.entities.Restaurant

/**
 * Initialize the database and verify whether the repository can provide the viewmodel with data:
 * - Test went successful
 * - Database is filled with more then 1 entry of restaurants
 * - ..
 * @author Jaspervl
 */
@RunWith(AndroidJUnit4::class)
class RestaurantViewModelTest {
    private lateinit var database: RestaurantDatabase
    private lateinit var restaurantDao: RestaurantDao

    private val restaurantCount = 10
    private val res = RestaurantUtil.generateRestaurants(restaurantCount)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RestaurantDatabase::class.java).build()
        restaurantDao = database.restaurantDao()
    }

    // Checks whether the worker populates the database successfully
    @Test
    fun testViewModel() {
//        Do stuff
    }

}