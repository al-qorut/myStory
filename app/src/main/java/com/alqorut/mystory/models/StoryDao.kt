package com.alqorut.mystory.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStory(story: Story)

    @Query("select * from story")
    fun getStory(): Flowable<MutableList<Story>>
}