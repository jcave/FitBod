package com.fitbod.demo.ui.workouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitbod.demo.R

class WorkoutsAdapter(private var workoutViewModels: List<WorkoutViewModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_workout, parent, false)

        return WorkoutHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindWorkoutHolder(holder as WorkoutHolder, position)
    }

    override fun getItemCount(): Int {
        return workoutViewModels.size
    }


    private fun bindWorkoutHolder(holder: WorkoutHolder, position: Int) {

        val workoutViewModel = workoutViewModels[position]

        holder.apply {
            workoutName.text = workoutViewModel.workoutName
            workoutRecord.text = workoutViewModel.oneRepMax.toString()

            itemView.setOnClickListener {
                val action =
                    WorkoutsFragmentDirections.actionWorkoutsToExercise(
                        workoutViewModel.workoutId,
                        workoutViewModel.workoutName,
                        workoutViewModel.oneRepMax
                    )
                it.findNavController().navigate(action)
            }

        }
    }

    fun update(viewModels: List<WorkoutViewModel>) {
        workoutViewModels = viewModels
        notifyDataSetChanged()
    }

    private class WorkoutHolder(view: View) : RecyclerView.ViewHolder(view) {

        val workoutName: TextView = view.findViewById(R.id.txtWorkout)
        val workoutRecord: TextView = view.findViewById(R.id.txtRecord)

    }

}