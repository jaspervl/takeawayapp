package com.jvl.assignment.database

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import com.jvl.assignment.model.RestaurantRepository
import androidx.work.ListenableWorker.Result
import com.jvl.assignment.model.RestaurantDatabase
import com.jvl.assignment.workers.RestaurantDatabaseWorker
import org.hamcrest.CoreMatchers.`is` as equalTo

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

/**
 * Initialize the database and verify whether the repository can provide the viewmodel with data:
 * - Test went successful
 * - Database is filled with more then 1 entry of restaurants
 * - ..
 */
@RunWith(AndroidJUnit4::class)
class PrepopulateDatabaseTest {
    private lateinit var context: Context

    @Before
    fun setupDatabase() {
        context = ApplicationProvider.getApplicationContext()

    }

    // Checks if worker succeeds in doings its job (prepopulating the database)
    @Test
    fun testWorker() {
        val worker = TestListenableWorkerBuilder<RestaurantDatabaseWorker>(ApplicationProvider.getApplicationContext()).build()
        val result = worker.startWork().get()
        assertThat(result, equalTo(Result.success()))
    }

}