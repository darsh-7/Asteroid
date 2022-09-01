package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.Repo
import retrofit2.HttpException

class RefreshWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshWorker"
    }


    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = Repo(database)

        return try {
            repository.refreshAsteroids()
            Result.success()

        } catch (e: HttpException) {
            Result.retry()
        }
    }
}