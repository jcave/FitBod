package com.fitbod.demo.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fitbod.demo.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseFragmentViewModel: ExerciseFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        exerciseFragmentViewModel =
            ViewModelProvider(this).get(ExerciseFragmentViewModel::class.java)

        exerciseFragmentViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return binding.root
    }
}