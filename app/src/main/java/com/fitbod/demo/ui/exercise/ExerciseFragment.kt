package com.fitbod.demo.ui.exercise

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.fitbod.demo.R
import com.fitbod.demo.databinding.FragmentExerciseBinding
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

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

        return binding.root
    }


    private fun updateUI(workoutData: WorkoutsWithUserWorkouts) {
        binding.workoutInfo.txtWorkout.text = workoutData.workout.name
        binding.workoutInfo.txtRecord.text =
            workoutData.userWorkouts.maxByOrNull { it.oneRepMax }?.oneRepMax.toString()

        val entries = workoutData.userWorkouts
            .sortedBy { it.date }
            .map {
            Entry(it.date.time.toFloat(), it.oneRepMax.toFloat())
        }

        entries.forEach {
            Log.i("OUTPUT", it.x.toString())
        }

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                val formatDate = SimpleDateFormat("MMM d")
                val dateString = formatDate.format(Date(value.toLong()))

                return dateString
            }
        }


        with(binding.dataChart) {
            axisLeft.isEnabled = true
            axisRight.isEnabled = true
            legend.isEnabled = true

            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)

            legend.textColor = requireContext().getColor(R.color.fitbod_red)

            xAxis.textColor = requireContext().getColor(R.color.greyDark)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = 6
            xAxis.valueFormatter = formatter
            xAxis.labelRotationAngle = -45f
            legend.isEnabled = false
            axisLeft.textColor = requireContext().getColor(R.color.greyDark)
        }

        val lineDataSet1 = LineDataSet(entries, "").apply {
            color = requireContext().getColor(R.color.fitbod_red)
            setDrawHighlightIndicators(true)
            lineWidth = 3f
            setCircleColor(R.color.fitbod_red)
            circleHoleColor = requireContext().getColor(R.color.fitbod_red)
            circleHoleRadius = 3f
            highLightColor = requireContext().getColor(R.color.fitbod_red)
        }


        val dataSets = arrayListOf<ILineDataSet>(lineDataSet1)

        val data: LineData = LineData(dataSets).apply {
            setValueTextColor(R.color.fitbod_red)

        }

        binding.dataChart.data = data
        binding.dataChart.invalidate()

    }
}