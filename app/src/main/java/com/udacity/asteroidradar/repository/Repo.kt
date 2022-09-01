package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ApiMang
import com.udacity.asteroidradar.api.asAsteroidEntities
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asAsteroids
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class Repo(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.AstDao.getAll()) {
            it.asAsteroids()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {

            val asteroids = ApiMang.getAsteroids()

            database.AstDao.insertAll(asteroids.asAsteroidEntities())
        }
    }

//    private fun filterAsteroids(ast : List<Asteroid>){
//        val calendar = Calendar.getInstance()
//        val sdf = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
//        val dateFormat =  sdf.format(Date())
//        var newAst : MutableList<Asteroid> = mutableListOf()
//        for (item in ast){
//
//            Log.i("AsteroidListAdapter","add new ast not here"+dateFormat.toString()+" -- " + item.closeApproachDate)
//
//            if (item.closeApproachDate == dateFormat.toString()) {
//                Log.i("AsteroidListAdapter","add new ast ok"+calendar.time.toString())
//                newAst.add(item)
//            }
//
//        }
//        ast.
//
//    }
    suspend fun clearAsteroids() {
        withContext(Dispatchers.IO) {
            database.AstDao.clearAll()
        }
    }

}