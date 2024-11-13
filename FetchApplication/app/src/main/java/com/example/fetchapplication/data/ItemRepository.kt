package com.example.fetchapplication.data

import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    val items: Flow<List<Item>>

    suspend fun fetchItems()
}