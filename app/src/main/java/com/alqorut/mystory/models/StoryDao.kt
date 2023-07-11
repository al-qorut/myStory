package com.alqorut.mystory.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alqorut.mystory.api.response.ListStoryItem
import io.reactivex.Flowable

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStory(story: List<ListStoryItem>)

    @Query("select * from list_story")
    fun getStory(): Flowable<List<ListStoryItem>>
}