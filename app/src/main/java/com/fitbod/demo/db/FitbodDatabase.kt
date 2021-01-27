package com.fitbod.demo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitbod.demo.db.dao.UserWorkoutDao
import com.fitbod.demo.db.dao.WorkoutDao
import com.fitbod.demo.db.models.UserWorkout
import com.fitbod.demo.db.models.Workout
import com.fitbod.demo.db.typeConverters.DateConverter

@Database(entities = [UserWorkout::class, Workout::class], version = 1, exportSchema = false)

@TypeConverters(DateConverter::class)
abstract class FitbodDatabase : RoomDatabase() {

    abstract fun userWorkoutDao(): UserWorkoutDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: FitbodDatabase? = null

        fun getDatabase(context: Context): FitbodDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitbodDatabase::class.java,
                    "fitbod_database"

                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }


}