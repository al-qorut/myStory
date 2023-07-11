package com.alqorut.mystory.models

import androidx.room.Entity

@Entity(tableName = "user", primaryKeys = ["email"])
class User(
    var email: String,
    var name: String,
    var password: String,
)