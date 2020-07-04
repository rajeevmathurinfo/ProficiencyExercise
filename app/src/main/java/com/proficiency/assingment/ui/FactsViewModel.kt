package com.proficiency.assingment.ui

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
import retrofit2.Response
import java.io.IOException


class FactsViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {
    val mutableLiveData: MutableLiveData<Resource<FactsResponse>> = MutableLiveData()
    init {
        getFacts()
    }

    fun getFacts() = viewModelScope.launch {
        mutableLiveData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<FactsAppliction>())) {
                val response = repository.getFacts()
                mutableLiveData.postValue(handleResponse(response))
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

    private fun handleResponse(response: Response<FactsResponse>): Resource<FactsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultReesponse ->
                deleteFacts()
                saveFacts(resultReesponse.rows)
                return Resource.Success(resultReesponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveFacts(row: ArrayList<FactsResponse.Row>) = viewModelScope.launch {
        repository.upsert(row)
    }

    fun getSavedFacts() = repository.getSavedFacts()

    fun deleteFacts() = viewModelScope.launch {
        repository.deleteFacts()
    }
}