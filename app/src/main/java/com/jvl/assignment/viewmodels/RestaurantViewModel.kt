package com.jvl.assignment.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jvl.assignment.model.RestaurantRepository
import com.jvl.assignment.model.entities.Restaurant
import com.jvl.assignment.utility.Metric
import com.jvl.assignment.utility.RestaurantComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel(context: Application) : AndroidViewModel(context) {
    private val repository = RestaurantRepository(context)

    // Query for filtering
    var query = MutableLiveData<String>("")

    var liveD = repository.getRestaurants("")
    // Original live data and the mediator livedata from the DAO
    private val filteredData = Transformations.switchMap(query) { repository.getRestaurants(it) }
    val restaurants = MediatorLiveData<List<Restaurant>>()

    // Currently selected metric to filter & the comparator for sorting
    private var metric = MutableLiveData<Metric>(Metric.BEST_MATCH)
    private val comparator = RestaurantComparator(metric)

    init {
        // Listen to changes in the filtered data
        restaurants.addSource(filteredData) { sortData(it) }

        // Listen to metric being changed
        restaurants.addSource(metric) {
            restaurants.value?.also {
                sortData(it)
            }
        }
    }


    fun setMetric(met: Metric) {
        if (metric.value != met) metric.value = met
    }

    // Sorts the list in a different thread
    private fun sortData(list: List<Restaurant>) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                restaurants.postValue(list.sortedWith(comparator).reversed())
            }
        }

    fun toggleFavorite(item: Restaurant) {
        viewModelScope.launch {
            item.favorite = !item.favorite
            repository.updateRestaurant(item)
        }
    }
}