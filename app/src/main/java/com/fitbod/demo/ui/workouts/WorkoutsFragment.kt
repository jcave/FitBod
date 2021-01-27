package com.fitbod.demo.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitbod.demo.databinding.FragmentWorkoutsBinding
import com.fitbod.demo.db.models.UserWorkout

class WorkoutsFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!

    private var workoutAdapter: WorkoutsAdapter? = null

    private lateinit var workoutsFragmentViewModel: WorkoutsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)

        workoutsFragmentViewModel =
            ViewModelProvider(this).get(WorkoutsFragmentViewModel::class.java)

        workoutsFragmentViewModel.workoutViewModels.observe(viewLifecycleOwner, { workoutViewModels ->
            updateAdapter(workoutViewModels)
            if (workoutViewModels.isEmpty()) {
                binding.buttonLoad.visibility = View.VISIBLE
            }
        })

        workoutAdapter = WorkoutsAdapter(emptyList())
        val workoutLayoutManager = LinearLayoutManager(requireContext())

        binding.recyclerview.apply {
            layoutManager = workoutLayoutManager
            adapter = workoutAdapter
        }

        binding.buttonLoad.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonLoad.visibility = View.INVISIBLE
            workoutsFragmentViewModel.populateDatabase()
        }

        return binding.root
    }

    private fun updateAdapter(userWorkouts: List<WorkoutViewModel>) {
        binding.progressBar.visibility = View.INVISIBLE
        binding.buttonLoad.visibility = View.INVISIBLE
        workoutAdapter?.update(userWorkouts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        workoutAdapter = null
    }
}