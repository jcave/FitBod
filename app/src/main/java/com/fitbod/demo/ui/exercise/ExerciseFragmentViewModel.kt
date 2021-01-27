package com.fitbod.demo.ui.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts
import com.fitbod.demo.db.repository.WorkoutRepository

class ExerciseFragmentViewModel(workoutId: Int, application: Application) :
    AndroidViewModel(application) {

    //    private val _text = MutableLiveData<String>().apply {
//        value = "This is slideshow Fragment"
//    }
//    val text: LiveData<String> = _text

    private val repository: WorkoutRepository = WorkoutRepository(application)
    val workoutWithUserWorkouts: LiveData<WorkoutsWithUserWorkouts> =
        repository.getWorkoutWithUserWorkouts(workoutId)



}