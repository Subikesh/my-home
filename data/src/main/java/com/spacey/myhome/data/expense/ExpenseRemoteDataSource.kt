package com.spacey.myhome.data.expense

import retrofit2.http.GET

interface ExpenseRemoteDataSource {
    @GET("42c5c50c01b5d0e377b0")
    suspend fun getDateExpenses(): ExpenseRemoteEntity
}