package com.fitbod.demo.ui.workouts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts
import com.fitbod.demo.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutsFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository = WorkoutRepository(application)
    private val workoutsWithUserWorkouts: LiveData<List<WorkoutsWithUserWorkouts>> =
        repository.workoutsWithUserWorkouts
    val workoutViewModels: LiveData<List<WorkoutViewModel>> =
        Transformations.map(workoutsWithUserWorkouts) { workoutData ->
            val viewModels = mutableListOf<WorkoutViewModel>()

            workoutData.forEach { workoutWithUserWorkout ->
                val viewModel = WorkoutViewModel().apply {
                    workoutId = workoutWithUserWorkout.workout.id
                    workoutName = workoutWithUserWorkout.workout.name
                    oneRepMax =
                        workoutWithUserWorkout.userWorkouts.maxOfOrNull { it.oneRepMax } ?: 0
                }

                viewModels.add(viewModel)
            }

            viewModels
        }


    fun populateDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.prePopulateDatabase(getApplication())
        }
    }

}