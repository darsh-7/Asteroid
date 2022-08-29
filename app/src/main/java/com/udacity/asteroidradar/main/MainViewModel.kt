package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi.getPictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidsRepository
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val repository = AsteroidsRepository(database)


    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>

        get() = _pictureOfDay

    private var _img = MutableLiveData<String>()
    val img: LiveData<String>
        get() = _img


    init {
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                val  pictureOfDay = AsteroidApi.getPictureOfDay().await()
                Log.i("MainViewModel","img url : "+pictureOfDay.url)

                _img.value = pictureOfDay.url

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}