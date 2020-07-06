package com.proficiency.assingment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proficiency.assingment.FactsAppliction
import com.proficiency.assingment.R
import com.proficiency.assingment.model.FactsResponse
import com.proficiency.assingment.repository.Repository
import com.proficiency.assingment.util.Resource
import com.proficiency.assingment.util.Utils
import kotlinx.coroutines.launch
import java.io.IOException

class FactsViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {
    val mutableLiveData: MutableLiveData<Resource<FactsResponse>> = MutableLiveData()
    private var viewModelProvider: ViewModelProvider = ViewModelProvider()

    init {
        getFacts()
    }

    fun getFacts() = viewModelScope.launch {
        mutableLiveData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<FactsAppliction>())) {
                val response = repository.getFacts()
                mutableLiveData.postValue(
                    viewModelProvider.handleResponse(
                        response,
                        repository,
                        this@FactsViewModel
                    )
                )
            } else {
                mutableLiveData.postValue(
                    Resource.Error(
                        getApplication<FactsAppliction>().getString(
                            R.string.no_internet
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> mutableLiveData.postValue(
                    Resource.Error(
                        getApplication<FactsAppliction>().getString(
                            R.string.network_falure
                        )
                    )
                )
                else -> mutableLiveData.postValue(
                    Resource.Error(
                        getApplication<FactsAppliction>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    fun getSavedFacts() = repository.getSavedFacts()

}