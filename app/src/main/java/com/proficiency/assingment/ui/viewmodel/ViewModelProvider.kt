package com.proficiency.assingment.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.proficiency.assingment.model.FactsResponse
import com.proficiency.assingment.repository.Repository
import com.proficiency.assingment.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModelProvider {
    fun handleResponse(
        response: Response<FactsResponse>,
        repository: Repository,
        viewModel: FactsViewModel
    ): Resource<FactsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultReesponse ->
                deleteFacts(repository, viewModel)
                saveFacts(repository, viewModel, resultReesponse.rows)
                return Resource.Success(resultReesponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun saveFacts(
        repository: Repository,
        viewModel: FactsViewModel,
        row: ArrayList<FactsResponse.Row>
    ) = viewModel.viewModelScope.launch {
        repository.upsert(row)
    }

    private fun deleteFacts(repository: Repository, viewModel: FactsViewModel) =
        viewModel.viewModelScope.launch {
            repository.deleteFacts()
        }
}