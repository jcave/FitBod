package com.fitbod.demo.ui.workouts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fitbod.demo.db.models.UserWorkout
import com.fitbod.demo.db.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutsFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository = WorkoutRepository(application)
    val userWorkouts: LiveData<List<UserWorkout>> = repository.getWorkouts

    fun populateDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.prePopulateDatabase(getApplication())
        }
    }

}