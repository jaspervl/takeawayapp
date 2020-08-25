package com.jvl.assignment.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jvl.assignment.model.RestaurantRepository
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.utility.Metric
import com.jvl.assignment.utility.RestaurantComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel(context: Application) : AndroidViewModel(context) {
    private val repository = RestaurantRepository(context)

    // Original live data and the mediator livedata from the DAO
    private val originalData = repository.getRestaurants()
    val restaurants = MediatorLiveData<List<Restaurant>>()

    // Currently selected metric to filter
    private var metric = MutableLiveData<Metric>().apply {
        value = Metric.BEST_MATCH
    }

    // Handles the sorting for us
    private val comparator = RestaurantComparator(metric)

    init {
        restaurants.addSource(originalData){
            viewModelScope.launch {
                sortData(it)
            }
        }
    }

    // Sorts the list in a different thread
    private suspend fun sortData(list: List<Restaurant>)=
        withContext(Dispatchers.IO){
            restaurants.postValue(list.sortedWith(comparator))
        }
}