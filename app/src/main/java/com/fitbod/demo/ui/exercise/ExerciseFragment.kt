package com.fitbod.demo.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.fitbod.demo.databinding.FragmentExerciseBinding
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts

class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseFragmentViewModel: ExerciseFragmentViewModel
    private val args: ExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        val workoutId = args.workoutId
        val viewModelFactory = ExerciseFragmentViewModelFactory(workoutId, activity?.application!!)
        exerciseFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(ExerciseFragmentViewModel::class.java)

        exerciseFragmentViewModel.workoutWithUserWorkouts.observe(viewLifecycleOwner, {
            updateUI(it)
        })


//        exerciseFragmentViewModel.text.observe(viewLifecycleOwner, Observer {
//
//        })


        return binding.root
    }


    private fun updateUI(workoutData: WorkoutsWithUserWorkouts) {
        binding.workoutInfo.txtWorkout.text = workoutData.workout.name
        binding.workoutInfo.txtRecord.text =
            workoutData.userWorkouts.maxByOrNull { it.oneRepMax }?.oneRepMax.toString()
    }
}