package com.fitbod.demo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleRegistry
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fitbod.demo.db.FitbodDatabase
import com.fitbod.demo.db.dao.UserWorkoutDao
import com.fitbod.demo.db.models.UserWorkout
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserWorkoutTests {

    private lateinit var userWorkoutDao: UserWorkoutDao
    private lateinit var db: FitbodDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FitbodDatabase::class.java
        ).build()
        userWorkoutDao = db.userWorkoutDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserWorkoutAndReadInList() {
        val userWorkout = UserWorkout(0, Date(), 1, 5, 100, 1)
        userWorkoutDao.insertAllUserWorkouts(listOf(userWorkout))

        userWorkoutDao.getUserWorkouts().observeForever {
            assertEquals(1, it.size)
            assertEquals(1, it.first().sets)
            assertEquals(5,  it.first().reps)
            assertEquals(100,  it.first().weight)
            assertEquals(1,  it.first().id)
            assertEquals(112,  it.first().oneRepMax)
        }

    }
}