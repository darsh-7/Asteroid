package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface RoomDao {

    @Query("select * from asteroid ORDER by closeApproachDate")
    fun getAll(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid WHERE closeApproachDate == :startDate AND closeApproachDate == :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsWithinTimeSpan(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<AsteroidEntity>)

//    @Query("DELETE FROM asteroid")
//    suspend fun clearAll()
}

@Database(version = 1, entities = [AsteroidEntity::class], exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val AstDao: RoomDao

    companion object {
        @Volatile
        private lateinit var instance: AsteroidDatabase

        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::instance.isInitialized) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid"
                    )
                        .build()
                }
            }
            return instance
        }
    }
}
