package com.fitbod.demo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitbod.demo.db.models.Workout

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkouts(workouts: List<Workout>)

    @Query("SELECT * FROM Workout WHERE id=:id ")
    fun getById(id: Int): LiveData<Workout>

    @Query("SELECT * FROM Workout")
    fun getAll(): List<Workout>

}