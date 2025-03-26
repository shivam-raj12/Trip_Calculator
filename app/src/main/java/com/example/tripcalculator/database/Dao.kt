package com.example.tripcalculator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM History ORDER BY date DESC")
    fun getAll(): Flow<List<History>>

    @Update
    suspend fun update(history: History)

    @Delete
    suspend fun delete(history: History)

}