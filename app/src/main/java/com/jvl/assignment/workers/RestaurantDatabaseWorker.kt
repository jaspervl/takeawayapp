package com.jvl.assignment.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.jvl.assignment.model.RestaurantDatabase
import com.jvl.assignment.model.entities.Restaurant
import kotlinx.coroutines.coroutineScope

/**
 * Responsible for the reading the test data and populating the database with it
 * @author Jaspervl
 */
class RestaurantDatabaseWorker (context:Context, params: WorkerParameters): CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        return coroutineScope {
            try {
                applicationContext.assets.open(DATA_FILENAME).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val holder = Gson().fromJson<RestaurantHolder>(jsonReader, RestaurantHolder::class.java)
                        Log.d(LOGGING_TAG, "Values: $holder)}")
                        RestaurantDatabase
                            .getInstance(applicationContext)
                            .restaurantDao()
                            .insertAll(holder.restaurants)
                        Result.success()
                    }
                }
            } catch (ex: Exception) {
                Log.e(LOGGING_TAG, "Error loading dummy data", ex)
                Result.failure()
            }
        }
    }

    data class RestaurantHolder(
        val restaurants: List<Restaurant>
    )
    companion object {
        // For now I just declared the constant locally without making a util class or the sorts for it.
        private const val DATA_FILENAME = "restaurant-data.json"
        private const val LOGGING_TAG = "Res-data-worker"
    }
}
