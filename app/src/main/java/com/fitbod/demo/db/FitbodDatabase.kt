package com.fitbod.demo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fitbod.demo.R
import com.fitbod.demo.db.dao.UserWorkoutDao
import com.fitbod.demo.db.dao.WorkoutDao
import com.fitbod.demo.db.models.UserWorkout
import com.fitbod.demo.db.models.Workout
import com.fitbod.demo.db.typeConverters.DateConverter
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Database(entities = [UserWorkout::class, Workout::class], version = 1, exportSchema = false)

@TypeConverters(DateConverter::class)
abstract class FitbodDatabase : RoomDatabase() {

    abstract fun userWorkoutDao(): UserWorkoutDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: FitbodDatabase? = null

        fun getDatabase(context: Context): FitbodDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitbodDatabase::class.java,
                    "fitbod_database"

                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            prePopulateDatabase(context)
                        }
                    })
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private fun prePopulateDatabase(context: Context) {

            var fileReader: BufferedReader? = null

            try {
                val customers = ArrayList<UserWorkout>()
                var line: String?

                val input: InputStream = context.resources.openRawResource(R.raw.workouts)
                fileReader = BufferedReader(InputStreamReader(input))
//                fileReader = BufferedReader(FileReader("workouts.csv"))

                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        val customer = Customer(
                            tokens[CUSTOMER_ID_IDX],
                            tokens[CUSTOMER_NAME_IDX],
                            tokens[CUSTOMER_ADDRESS_IDX],
                            Integer.parseInt(tokens[CUSTOMER_AGE])
                        )
                        customers.add(customer)
                    }

                    line = fileReader.readLine()
                }

                // Print the new customer list
                for (customer in customers) {
                    println(customer)
                }
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                } catch (e: IOException) {
                    println("Closing fileReader Error!")
                    e.printStackTrace()
                }
            }

        }
    }


}