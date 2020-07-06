package com.proficiency.assingment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proficiency.assingment.model.FactsResponse

@Database(
    entities = [FactsResponse.Row::class],
    version = 1
)
abstract class FactsDatabase : RoomDatabase() {
    abstract fun getFacsDao(): FactsDao

    companion object {
        private var instance: FactsDatabase? = null
        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FactsDatabase::class.java,
                "facts_db.db"
            ).build()
    }

}