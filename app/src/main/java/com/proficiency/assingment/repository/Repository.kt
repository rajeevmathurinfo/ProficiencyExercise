package com.proficiency.assingment.repository

import com.proficiency.assingment.api.RetrofitInstance

class Repository {
    suspend fun getFacts() = RetrofitInstance.api.getFacts()
}