package com.example.tripcalculator.database

import kotlinx.coroutines.flow.Flow

class Repository(private val dao: Dao) : Dao{

    override suspend fun insert(history: History) = dao.insert(history)

    override fun getAll(): Flow<List<History>> = dao.getAll()

    override suspend fun update(history: History) = dao.update(history)

    override suspend fun delete(history: History) = dao.delete(history)

}