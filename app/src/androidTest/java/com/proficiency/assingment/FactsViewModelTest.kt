package com.proficiency.assingment

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.proficiency.assingment.db.FactsDao
import com.proficiency.assingment.db.FactsDatabase
import com.proficiency.assingment.model.FactsResponse
import com.proficiency.assingment.repository.Repository
import com.proficiency.assingment.ui.FactsViewModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsViewModelTest {
    lateinit var viewModel: FactsViewModel
    lateinit var appDatabase: FactsDatabase
    lateinit var factsDao: FactsDao
    lateinit var instrumentationContext: Context


    @Before
    fun setUp() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context.applicationContext
        appDatabase = Room.inMemoryDatabaseBuilder(instrumentationContext, FactsDatabase::class.java).build()
        factsDao = appDatabase.getFacsDao()

        val repository = Repository(appDatabase)
        viewModel = FactsViewModel(instrumentationContext as FactsAppliction,
            repository
        )
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeFactsAndReadInList() {
        runBlocking {
            factsDao.upsert(FactsResponse.Row(1, "testDec", "", "TestTitle"))
            val facts = factsDao.getAllFacts()
            assertEquals(facts.value?.get(0)?.description, equalTo("testDec"))
        }
    }

}