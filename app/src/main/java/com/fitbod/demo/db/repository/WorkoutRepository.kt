package com.fitbod.demo.db.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.fitbod.demo.R
import com.fitbod.demo.db.FitbodDatabase
import com.fitbod.demo.db.models.UserDataRecord
import com.fitbod.demo.db.models.UserWorkout
import com.fitbod.demo.db.models.Workout
import com.fitbod.demo.db.models.WorkoutsWithUserWorkouts
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkoutRepository(application: Application) {

    private val db = FitbodDatabase.getDatabase(application)
    private val userWorkoutDao = db.userWorkoutDao()
    private val workoutDao = db.workoutDao()
    private val workoutsWithUserWorkoutsDao = db.workoutsWithUserWorkoutsDao()

    val workoutsWithUserWorkouts: LiveData<List<WorkoutsWithUserWorkouts>> =
        workoutsWithUserWorkoutsDao.getWorkoutsWithUserWorkouts()

    private fun insertUserWorkouts(userWorkouts: List<UserWorkout>) {
        userWorkoutDao.insertAllUserWorkouts(userWorkouts)
    }

    private fun insertWorkouts(workouts: List<Workout>) {
        workoutDao.insertWorkouts(workouts)
    }

    private fun getWorkouts(): List<Workout> {
        return workoutDao.getAll()
    }

    fun getWorkoutWithUserWorkouts(workoutId: Int): LiveData<WorkoutsWithUserWorkouts> {
        return workoutsWithUserWorkoutsDao.getWorkoutWithUserWorkouts(workoutId)
    }

    fun prePopulateDatabase(context: Context) {
        var fileReader: BufferedReader? = null

        try {
            val userDataRecords = ArrayList<UserDataRecord>()
            var line: String?
            val input: InputStream = context.resources.openRawResource(R.raw.workouts)

            fileReader = BufferedReader(InputStreamReader(input))
            line = fileReader.readLine()

            while (line != null) {
                val tokens = line.split(",")
                if (tokens.isNotEmpty()) {
                    val userDataRecord = UserDataRecord(
                        tokens[0],
                        tokens[1],
                        tokens[2].toInt(),
                        tokens[3].toInt(),
                        tokens[4].toInt()
                    )
                    userDataRecords.add(userDataRecord)
                }
                line = fileReader.readLine()
            }
            parseWorkouts(userDataRecords)
        } catch (e: Exception) {
            Log.i("WORKOUT", "Reading CSV Error!")
            e.printStackTrace()
        } finally {
            try {
                fileReader!!.close()
            } catch (e: IOException) {
                Log.i("WORKOUT", "Closing fileReader Error!")
                e.printStackTrace()
            }
        }
    }

    private fun parseWorkouts(userDataRecords: List<UserDataRecord>) {
        val workoutNames = userDataRecords.map { it.name }.toSet()
        val workouts = mutableListOf<Workout>()

        workoutNames.forEach { workoutName ->
            workouts.add(Workout(name = workoutName))
        }

        insertWorkouts(workouts)
        parseUserData(userDataRecords)
    }

    private fun parseUserData(userDataRecords: List<UserDataRecord>) {
        val userWorkouts = mutableListOf<UserWorkout>()

        val workoutHash: HashMap<String, Int> = hashMapOf()
        getWorkouts().forEach {
            workoutHash[it.name] = it.id
        }

        userDataRecords.forEach { dataRecord ->
            val userWorkout = UserWorkout().apply {
                sets = dataRecord.sets
                reps = dataRecord.reps
                weight = dataRecord.weight
                oneRepMax = calcOneRM(dataRecord.reps, dataRecord.weight)
                workoutId = workoutHash[dataRecord.name] ?: 0
                date = parseDate(dataRecord.date) ?: Date()
            }

            userWorkouts.add(userWorkout)
        }

        insertUserWorkouts(userWorkouts)

    }

    private fun calcOneRM(reps: Int, weight: Int): Int {
        return (weight.toDouble() * (36.0 / (37.0 - reps))).toInt()
    }

    private fun parseDate(date: String): Date? {
        //"Nov 07 2016"
        val originalDate = SimpleDateFormat("MMM dd yy")
        return originalDate.parse(date)
    }

}