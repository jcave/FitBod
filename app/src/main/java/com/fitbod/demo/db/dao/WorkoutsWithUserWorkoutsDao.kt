package com.fitbod.demo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts

@Dao
interface WorkoutsWithUserWorkoutsDao {

    @Transaction
    @Query("SELECT * FROM Workout")
    fun getWorkoutsWithUserWorkouts(): LiveData<List<WorkoutsWithUserWorkouts>>

    @Transaction
    @Query("SELECT * FROM Workout WHERE id=:workoutId LIMIT 1")
    fun getWorkoutWithUserWorkouts(workoutId: Int): LiveData<WorkoutsWithUserWorkouts>
}