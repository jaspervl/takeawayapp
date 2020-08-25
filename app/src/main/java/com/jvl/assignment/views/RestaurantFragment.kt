package com.jvl.assignment.views

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_restaurant.*

/**
 * A fragment representing a list of Items.
 */
class RestaurantFragment : Fragment() {

    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        val restaurantAdapter = RestaurantRecyclerAdapter(restaurantViewModel)

        binding.restaurantList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantAdapter
        }

        // Add observer
        restaurantViewModel.restaurants.observe(viewLifecycleOwner) {
            restaurantAdapter.submitList(it)
        }

        binding.editQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                // Do nothing
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                restaurantViewModel.query.value = p0
                Log.d("Fragment-c", "${restaurantViewModel.query.value}")
                return false
            }

        })
        return binding.root
    }
}