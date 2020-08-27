package com.jvl.assignment.model.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvl.assignment.model.entities.SortingValues

/**
 * Entity representing a restaurant from the provided testdata
 */
@Entity
data class Restaurant(
    @PrimaryKey
    val name: String,
    val status:String,
    var favorite: Boolean = false,

    // Contains the metrics for sorting
    @Embedded val sortingValues: SortingValues
)