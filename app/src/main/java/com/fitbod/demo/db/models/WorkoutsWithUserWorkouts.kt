package com.fitbod.demo.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutsWithUserWorkouts(

    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val userWorkouts: List<UserWorkout>

)