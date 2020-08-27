package com.jvl.assignment.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jvl.assignment.databinding.FragmentRestaurantBinding
import com.jvl.assignment.utility.Metric
import com.jvl.assignment.utility.RestaurantRecyclerAdapter
import com.jvl.assignment.viewmodels.RestaurantViewModel
import kotlinx.android.synthetic.main.fragment_restaurant.*
import java.util.*
import kotlin.concurrent.schedule


/**
 * Main screen on which the restaurants are displayed and the corresponding filter/search options
 */
class RestaurantFragment : Fragment() {

    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        val restaurantAdapter = RestaurantRecyclerAdapter(restaurantViewModel)
        val manager = LinearLayoutManager(context)

        binding.restaurantList.apply {
            layoutManager = manager
            adapter = restaurantAdapter
        }

        // Add observer
        restaurantViewModel.restaurants.observe(viewLifecycleOwner) {
            restaurantAdapter.submitList(it)

            /**
             * FIXME Currently, when the application is initialized, the livedata won't
             * load the initial objects properly until either:
             * - An external change is done( database inspector)
             * - A new query is executed on the database.
             *
             * I've added a refresh/reset button to the application to make it still testable.
             */
            binding.refreshContainer.apply {
                if (it.isEmpty()) {
                    visibility = View.VISIBLE
                    alpha = 0.0f
                    animate().alpha(1.0f).startDelay = 400
                } else {
                    visibility = View.GONE
                }

            }
        }

        // When something changes in the list (sorting,filter,favorites), scroll back to the top
        restaurantAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                restaurantList.scrollToPosition(0)
            }
        })

        // Add listener to search view
        binding.editQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = changeQuery(p0)
            override fun onQueryTextChange(p0: String?) = changeQuery(p0)

            private fun changeQuery(query: String?): Boolean {
                restaurantViewModel.query.value = query
                return false
            }
        })

        // Selection of the right metric depending on the clicked id
        binding.filterGroup.setOnCheckedChangeListener { _, id ->
            val selectedMetric = when (id) {
                binding.filterBestMatch.id -> Metric.BEST_MATCH
                binding.filterNewest.id -> Metric.NEWEST
                binding.filterRatingAverage.id -> Metric.RATING_AVERAGE
                binding.filterDistance.id -> Metric.DISTANCE
                binding.filterPopularity.id -> Metric.POPULARITY
                binding.filterAverageProductPrice.id -> Metric.AVERAGE_PRODUCT_PRICE
                binding.filterDeliveryCost.id -> Metric.DELIVERY_COST
                binding.filterMinCost.id -> Metric.MIN_COST
                else -> Metric.BEST_MATCH
            }
            restaurantViewModel.setMetric(selectedMetric)
        }

        // TODO Refresh button should be removed once the issue with livedata is resolved
        binding.refreshButton.setOnClickListener {
            binding.editQuery.setQuery("", true)
        }
        return binding.root
    }
}