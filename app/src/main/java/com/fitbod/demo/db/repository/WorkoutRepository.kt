package com.fitbod.demo.db.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.fitbod.demo.R
import com.fitbod.demo.db.FitbodDatabase
import com.fitbod.demo.db.models.UserWorkout
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

class WorkoutRepository(application: Application) {

    private val db = FitbodDatabase.getDatabase(application)
    private val userWorkoutDao = db.userWorkoutDao()

    val getWorkouts: LiveData<List<UserWorkout>> = userWorkoutDao.getUserWorkouts()

    fun insertUserWorkouts(userWorkouts: List<UserWorkout>) {
        userWorkoutDao.insertAllUserWorkouts(userWorkouts)
    }

    suspend fun prePopulateDatabase(context: Context) {

        var fileReader: BufferedReader? = null

        try {
            val userWorkouts = ArrayList<UserWorkout>()
            var line: String?

            val input: InputStream = context.resources.openRawResource(R.raw.workouts)
            fileReader = BufferedReader(InputStreamReader(input))
//                fileReader = BufferedReader(FileReader("workouts.csv"))

//                // Read CSV header
//                fileReader.readLine()

            // Read the file line by line starting from the second line
            line = fileReader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.isNotEmpty()) {
                    val userWorkout = UserWorkout(
                        0,
                        Date(), //tokens[0],
                        tokens[2].toInt(),
                        tokens[3].toInt(),
                        tokens[4].toInt(),
                        0 //tokens[2]
                    )
                    userWorkouts.add(userWorkout)
                }

                line = fileReader.readLine()
            }

            // Print the new customer list
            Log.i("WORKOUT", "Count: ${userWorkouts.size}")
            for (userWorkout in userWorkouts) {
                Log.i("WORKOUT", "user workout: ${userWorkout.weight}")
            }

            userWorkoutDao.insertAllUserWorkouts(userWorkouts)

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

}