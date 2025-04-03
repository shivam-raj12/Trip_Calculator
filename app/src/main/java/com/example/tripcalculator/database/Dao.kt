package com.example.tripcalculator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM History ORDER BY date DESC")
    fun getAll(): Flow<List<History>>

    @Delete
    suspend fun delete(history: History)

}