package com.example.fetchapplication.data

import retrofit2.http.GET

interface FetchApi {

    @GET("hiring.json")
    suspend fun getItems() : List<Item>
}