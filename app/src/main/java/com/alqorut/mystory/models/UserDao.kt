package com.alqorut.mystory.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Query("select * from user where email=:email and password=:pass")
    fun getUser(email: String, pass :String) : Flowable<List<User>>
}