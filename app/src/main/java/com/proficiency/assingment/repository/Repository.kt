package com.proficiency.assingment.repository

import com.proficiency.assingment.api.RetrofitInstance
import com.proficiency.assingment.db.FactsDatabase
import com.proficiency.assingment.model.FactsResponse

class Repository(val db: FactsDatabase) {
    suspend fun getFacts() = RetrofitInstance.api.getFacts()

    suspend fun upsert(row: FactsResponse.Row) = db.getFacsDao().upsert(row)

    fun getSavedFacts() = db.getFacsDao().getAllFacts()

    suspend fun deleteFacts() = db.getFacsDao().deleteAllFacts()
}