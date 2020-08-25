package com.jvl.assignment.viewmodels

import android.app.Application
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

    // Original live data and the mediator livedata from the DAO, initially we don't care about the specific quarry
    private val filteredData = Transformations.switchMap(query){ repository.getRestaurants(it) }
    val restaurants = MediatorLiveData<List<Restaurant>>()

    // Currently selected metric to filter & the comparator for sorting
    private var metric = MutableLiveData<Metric>(Metric.BEST_MATCH)
    private val comparator = RestaurantComparator(metric)

    init {
        restaurants.addSource(filteredData) {
            viewModelScope.launch {
                sortData(it)
            }
        }
    }

    // Sorts the list in a different thread
    private suspend fun sortData(list: List<Restaurant>) =
        withContext(Dispatchers.IO) {
            restaurants.postValue(list.sortedWith(comparator).reversed())
        }

    fun toggleFavorite(item: Restaurant) {
        viewModelScope.launch {
            item.favorite = !item.favorite
            repository.updateRestaurant(item)
        }
    }
}