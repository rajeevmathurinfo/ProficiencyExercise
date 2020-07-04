package com.proficiency.assingment.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.proficiency.assingment.model.FactsResponse

@Dao
interface FactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(factsRow: ArrayList<FactsResponse.Row>)

    @Query("SELECT * FROM facts")
    fun getAllFacts(): LiveData<List<FactsResponse.Row>>

    @Query("DELETE FROM facts")
    suspend fun deleteAllFacts()
}