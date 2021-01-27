package com.fitbod.demo.ui.workouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitbod.demo.R
import com.fitbod.demo.db.models.UserWorkout

class WorkoutsAdapter(private var workoutViewModels: List<UserWorkout>) :
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
            workoutName.text = workoutViewModel.reps.toString()
            workoutRecord.text = workoutViewModel.weight.toString()

            itemView.setOnClickListener {
//                val action = WorkoutsFragmentDirections.ac(workoutViewModel.id)
//                it.findNavController().navigate(action)
            }

        }
    }

    fun update(viewModels: List<UserWorkout>) {
        workoutViewModels = viewModels
        notifyDataSetChanged()
    }

    private class WorkoutHolder(view: View) : RecyclerView.ViewHolder(view) {

        val workoutName: TextView = view.findViewById(R.id.txtWorkout)
        val workoutRecord: TextView = view.findViewById(R.id.txtRecord)

    }

}