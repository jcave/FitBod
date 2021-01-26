package com.fitbod.demo.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitbod.demo.databinding.FragmentWorkoutsBinding

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

        workoutsFragmentViewModel.text.observe(viewLifecycleOwner, Observer {
            //
        })

        workoutAdapter = WorkoutsAdapter(emptyList())
        val workoutLayoutManager = LinearLayoutManager(requireContext())

        binding.recyclerview.apply {
            layoutManager = workoutLayoutManager
            adapter = workoutAdapter
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        workoutAdapter = null
    }
}