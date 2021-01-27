package com.fitbod.demo.ui.exercise

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ExerciseFragmentViewModelFactory(
    private val workoutId: Int,
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseFragmentViewModel::class.java)) {
            return ExerciseFragmentViewModel(workoutId, application) as T
        }

        throw IllegalArgumentException("ViewModel not assignable")
    }
}