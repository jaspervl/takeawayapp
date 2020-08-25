package com.jvl.assignment.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jvl.assignment.model.entities.Restaurant

/**
 * Even though the only data source currently in the application, it is still handy to have this
 * abstraction layer if in the future new sources will be added (Like an API datasource with retrofit)
 * @author Jaspervl
 */
class RestaurantRepository (context: Context){
    // Only the dao for restaurant items is necessary for now
    private val restaurantDao by lazy {
        RestaurantDatabase
            .getInstance(context)
            .restaurantDao()
    }

    fun getRestaurants(): LiveData<List<Restaurant>> = restaurantDao.retrieveAll()
}