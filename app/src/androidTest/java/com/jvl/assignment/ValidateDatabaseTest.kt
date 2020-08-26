package com.jvl.assignment

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import com.jvl.assignment.model.RestaurantRepository
import androidx.work.ListenableWorker.Result
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
 * @author Jaspervl
 */
@RunWith(AndroidJUnit4::class)
class ValidateDatabaseTest {
    private lateinit var context:Context
    private lateinit var repository: RestaurantRepository

    @Before
    fun setupDatabase(){
        context =ApplicationProvider.getApplicationContext()
        repository = RestaurantRepository(context)
    }

    @Test
    fun testWorker() {
        val worker = TestListenableWorkerBuilder<RestaurantDatabaseWorker>(context).build()
        assertThat(worker.startWork().get() ,equalTo(Result.success()))
    }

}