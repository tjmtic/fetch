package com.example.fetchapplication.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class ItemRepositoryImpl @Inject constructor(val itemApi: FetchApi) : ItemRepository {

    //In-memory Data Implementation taking place of DB
    private val _items: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    override val items: Flow<List<Item>> = _items

    override suspend fun fetchItems() {
        _items.value = repoFetchItems()
    }

    private suspend fun repoFetchItems(): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                itemApi.getItems()

            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}