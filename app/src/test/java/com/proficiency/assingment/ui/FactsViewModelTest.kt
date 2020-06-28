package com.proficiency.assingment.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.proficiency.assingment.repository.Repository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FactsViewModelTest {
    lateinit var viewModel: FactsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = Repository()
        viewModel = FactsViewModel(context as Application, repository)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun getMutableLiveData() {

    }

    @Test
    fun getFacts() {

    }
}