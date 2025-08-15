package com.example.anew.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)
    @Query("SELECT * From users WHERE Username = :username")
    suspend  fun getUserByUsername(username: String): User?
    @Query("SELECT * From users WHERE Email = :email")
    suspend  fun getUserByEmail(email: String): User?



}