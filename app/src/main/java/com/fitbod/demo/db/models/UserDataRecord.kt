package com.fitbod.demo.db.models

data class UserDataRecord(
    var date: String = "",
    var name: String = "",
    var sets: Int = 0,
    var reps: Int = 0,
    var weight: Int = 0
)