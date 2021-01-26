package com.fitbod.demo.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Workout(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = ""

)