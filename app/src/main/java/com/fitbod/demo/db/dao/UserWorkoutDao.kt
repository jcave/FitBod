package com.fitbod.demo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitbod.demo.db.models.UserWorkout

@Dao
interface UserWorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUserWorkouts(userWorkouts: List<UserWorkout>)

    @Query("SELECT * FROM UserWorkout WHERE workoutId=:workoutId ")
    fun getByWorkoutId(workoutId: Int): LiveData<List<UserWorkout>>

}