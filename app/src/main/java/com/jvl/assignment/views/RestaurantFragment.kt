package com.jvl.assignment.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.jvl.assignment.utility.RestaurantRecyclerAdapter
import com.jvl.assignment.databinding.FragmentRestaurantBinding
import com.jvl.assignment.viewmodels.RestaurantViewModel

/**
 * A fragment representing a list of Items.
 */
class RestaurantFragment : Fragment() {

    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        val restaurantAdapter =
            RestaurantRecyclerAdapter()

        initObserver(restaurantAdapter)
        binding.restaurantList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantAdapter
        }

        binding.editQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                // Do nothing
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        return binding.root
    }

    // Observe the livedata in the viewmodel
    private fun initObserver(adapter: RestaurantRecyclerAdapter) =
        restaurantViewModel.restaurants.observe(viewLifecycleOwner) { adapter.submitList(it) }

}