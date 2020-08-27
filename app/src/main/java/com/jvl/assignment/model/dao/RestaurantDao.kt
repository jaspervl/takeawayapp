package com.jvl.assignment.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jvl.assignment.model.entities.Restaurant
import kotlinx.coroutines.flow.Flow

/**
 * Restaurant dao with all possible queries and database operation for the time being
 */
@Dao
interface RestaurantDao {

    // Initially this was done using varargs, but it failed importing arrays/lists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(restaurants: List<Restaurant>)

    @Insert
    fun insert(restaurant: Restaurant)

    @Update
    suspend fun update(restaurant: Restaurant)

    @Query("SELECT * FROM Restaurant WHERE name LIKE '%'|| :search || '%'")
    fun retrieveAll(search: String): LiveData<List<Restaurant>>
}