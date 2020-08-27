package com.jvl.assignment.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jvl.assignment.model.dao.RestaurantDao
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.model.entities.SortingValues
import com.jvl.assignment.workers.RestaurantDatabaseWorker


/**
 * The database consisting of the restaurant data with the corresponding sorting values
 */
@Database(entities = [Restaurant::class], version = 1, exportSchema = false)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {
        private const val DATABASE_NAME = "restaurant-db"
        @Volatile private var instance: RestaurantDatabase? = null

        fun getInstance(context: Context): RestaurantDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, RestaurantDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            OneTimeWorkRequestBuilder<RestaurantDatabaseWorker>()
                                .build()
                                .also { WorkManager.getInstance(context).enqueue(it) }
                        }
                    }).build()
            }
        }
    }
}