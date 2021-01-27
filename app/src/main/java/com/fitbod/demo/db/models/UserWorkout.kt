package com.fitbod.demo.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserWorkout(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var date: Date = Date(),
    var sets: Int = 0,
    var reps: Int = 0,
    var weight: Int = 0,
    var workoutId: Int = 0,

    ) {

    val oneRepMax: Int get() = (weight.toDouble() * (36.0 / (37.0 - reps))).toInt()
}