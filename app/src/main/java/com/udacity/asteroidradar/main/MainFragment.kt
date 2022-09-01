package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.img.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", "img changed " + it)
            Picasso.get().load(it).into(binding.activityMainImageOfTheDay)

        })
        viewModel.imgDescriptions.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", "img descriptions changed " + it)
            val strFormat = binding.activityMainImageOfTheDay.resources.getString(
                R.string.nasa_picture_of_day_content_description_format)

            binding.activityMainImageOfTheDay.contentDescription = String.format(strFormat, it)
        })



        val adapter = AsteroidListAdapter(AsteroidClickListener { asteroidId ->
            viewModel.onAsteroidItemClick(asteroidId)
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.navToDetailFrag.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onDetailFragmentNavigated()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.show_rent_menu) {
            Log.i("MainFragment","show_rent_menu called")
            viewModel.clearAsteroids()
            Constants.DAYS_VIEW = 0
            viewModel.refreshAsteroids()
        }
        else if(item.itemId==R.id.show_all_menu) {
            Log.i("MainFragment","show_rent_menu called")
            viewModel.clearAsteroids()
            Constants.DAYS_VIEW = 7
            viewModel.refreshAsteroids()
        }

        return true
    }

}
