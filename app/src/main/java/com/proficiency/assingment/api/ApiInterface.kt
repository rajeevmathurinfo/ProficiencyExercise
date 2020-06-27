package com.proficiency.assingment.api

import com.proficiency.assingment.model.FactsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/s/2iodh4vg0eortkl/facts.json")
    suspend fun getFacts(): Response<FactsResponse>
}