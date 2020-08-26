package com.jvl.assignment.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jvl.assignment.model.entities.Restaurant

/**
 * Restaurant dao with all possible queries and database operation for the time being
 * @author Jaspervl
 */
@Dao
interface RestaurantDao {

    // Initially this was done using varargs, but it failed importing arrays/lists
    @Insert
    fun insertAll(restaurants: List<Restaurant>)

    @Insert
    fun insert(restaurant: Restaurant)

    @Update
    suspend fun update(restaurant: Restaurant)

    @Query("SELECT * FROM Restaurant WHERE name LIKE '%'|| :search || '%'")
    fun retrieveAll(search: String): LiveData<List<Restaurant>>
}