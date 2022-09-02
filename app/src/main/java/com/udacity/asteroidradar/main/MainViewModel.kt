package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ApiMang
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asAsteroids
import com.udacity.asteroidradar.repository.Repo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AsteroidDatabase.getInstance(app)
    private val repository = Repo(database)
    var asteroids = repository.asteroids

    var img = MutableLiveData<String>()
    var imgDescriptions = MutableLiveData<String>()

    private val _navToDetailFrag = MutableLiveData<Asteroid?>()
    val navToDetailFrag
        get() = _navToDetailFrag

    init {
        refreshAsteroids()
        getPictureOfDay()
    }


    fun onAsteroidItemClick(data: Asteroid) {
        _navToDetailFrag.value = data
    }

    fun onDetailFragmentNavigated() {
        _navToDetailFrag.value = null
    }

    fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun clearAsteroids() {
//        viewModelScope.launch {
//            try {
//                repository.clearAsteroids()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        Log.i("MainViewModel", "in my view")
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
//                val  pictureOfDay = ApiMang.getPictureOfDay().await()
                val pictureOfDay = ApiMang.getPictureOfDay()
                Log.i("MainViewModel", "img url : " + pictureOfDay.url)

                img.value = pictureOfDay.url
                imgDescriptions.value = pictureOfDay.title

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun filterAsteroids(endDate: Int = 7) {

        val sdf = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
        val dateFormat = sdf.format(Date())

        Log.i("MainViewModel", "change list data to" + dateFormat.toString() )
        asteroids = if (endDate == 1) {
            Transformations.map(
                database.AstDao.getAsteroidsWithinTimeSpan(
                    dateFormat.toString(),
                    dateFormat.toString()
                )
            ) {
                it.asAsteroids()
            }
        } else {
            Transformations.map(database.AstDao.getAll()) {
                it.asAsteroids()
            }
        }

    }


}