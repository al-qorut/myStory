package com.alqorut.mystory.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "story")
data class Story(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var email : String,
    var photoUrl : String,
    var description : String,
    var date : String
)
