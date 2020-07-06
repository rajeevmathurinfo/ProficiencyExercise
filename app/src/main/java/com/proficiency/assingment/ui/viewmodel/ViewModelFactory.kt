package com.proficiency.assingment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proficiency.assingment.repository.Repository

class ViewModelFactory(val app: Application, private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FactsViewModel(
            app,
            repository
        ) as T
    }
}