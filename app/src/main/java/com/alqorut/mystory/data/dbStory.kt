package com.alqorut.mystory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alqorut.mystory.models.Story
import com.alqorut.mystory.models.StoryDao
import com.alqorut.mystory.models.User
import com.alqorut.mystory.models.UserDao

@Database(entities = [User::class,Story::class], version = 1, exportSchema = false)
abstract class dbStory : RoomDatabase(){

    abstract fun story():StoryDao
    abstract fun user():UserDao

    companion object{
        var Instance : dbStory?=null
        fun getIntance(context: Context):dbStory?{
            if(Instance==null){
                synchronized(dbStory::class){
                    Instance = Room.databaseBuilder(context.applicationContext, dbStory::class.java,"data.db").build()
                }
            }
            return Instance
        }
    }
}