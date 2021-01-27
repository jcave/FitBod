package com.fitbod.demo.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.fitbod.demo.R
import com.fitbod.demo.databinding.FragmentExerciseBinding
import com.fitbod.demo.db.models.UserWorkout
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*

class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseFragmentViewModel: ExerciseFragmentViewModel
    private val args: ExerciseFragmentArgs by navArgs()

    private val xValueFormatter: ValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val formatDate = SimpleDateFormat("MMM dd")
            return formatDate.format(Date(value.toLong()))
        }
    }

    private val yValueFormatter: ValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return "${value.toInt()} lbs"
        }
    }

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

        binding.workoutInfo.txtRecord.text = args.workoutRepMax.toString()
        binding.workoutInfo.txtWorkout.text = args.workoutName

        return binding.root
    }

    private fun updateUI(workoutData: WorkoutsWithUserWorkouts) {
        binding.workoutInfo.txtWorkout.text = workoutData.workout.name
        binding.workoutInfo.txtRecord.text =
            workoutData.userWorkouts.maxByOrNull { it.oneRepMax }?.oneRepMax.toString()

        val colorIdRed = requireContext().getColor(R.color.fitbod_red)
        val colorIdGrey = requireContext().getColor(R.color.greyDark)

        with(binding.dataChart) {
            setTouchEnabled(true)
            isDragEnabled = true
            isScaleXEnabled = true
            isScaleYEnabled = false
            description.isEnabled = false
            extraBottomOffset = 10f

            axisLeft.isEnabled = true
            axisLeft.textColor = colorIdGrey
            axisLeft.valueFormatter = yValueFormatter
            axisLeft.textSize = 13f
            axisLeft.granularity = 15f
            axisLeft.isGranularityEnabled = true

            axisRight.isEnabled = false

            xAxis.textColor = colorIdGrey
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = xValueFormatter
            xAxis.textSize = 13f

            legend.textColor = colorIdRed
            legend.isEnabled = false
        }

        val entries = calculateDataSetEntries(workoutData.userWorkouts)
        val dataSet = LineDataSet(entries, "").apply {
            color = colorIdRed
            setDrawHighlightIndicators(true)
            lineWidth = 2f
            circleRadius = 5f
            setCircleColor(colorIdRed)
            circleHoleColor = colorIdRed
            setDrawCircleHole(true)
            highLightColor = colorIdRed
            setDrawValues(false)
        }

        val dataSets = arrayListOf<ILineDataSet>(dataSet)
        val data = LineData(dataSets)

        binding.dataChart.data = data
        binding.dataChart.invalidate()
        binding.dataChart.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun calculateDataSetEntries(userWorkouts: List<UserWorkout>): List<Entry> {
        // grouping all entries by the day they were on.

        val output = userWorkouts.groupBy({
            val formatDate = SimpleDateFormat("MM/dd/yyyy")
            formatDate.format(it.date)
        }, { it })

        return output.keys.map { key ->
            val formatDate = SimpleDateFormat("MM/dd/yyyy")
            val date = formatDate.parse(key)
            val max = output[key]?.maxByOrNull { it.oneRepMax }?.oneRepMax

            Entry(date!!.time.toFloat(), max!!.toFloat())
        }.sortedBy {
            it.x
        }
    }
}