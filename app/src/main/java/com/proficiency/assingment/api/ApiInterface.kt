package com.proficiency.assingment.api

import com.proficiency.assingment.model.FactsResponse
import com.proficiency.assingment.util.Constants.Companion.END_POINT_FACTS
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET(END_POINT_FACTS)
    suspend fun getFacts(): Response<FactsResponse>
}