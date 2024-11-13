package com.example.fetchapplication.feature.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchapplication.data.Item
import com.example.fetchapplication.data.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(val itemRepository: ItemRepository): ViewModel() {
    companion object {
        private val TAG = ItemViewModel::class.java.simpleName
    }

    private val items: StateFlow<Map<Int, List<Item>>> = itemRepository.items
        .map { list -> list
            //Remove Null or Blank items
            .filter { !it.name.isNullOrBlank() }
            //Group items by "listId"
            .groupBy { it.listId }
        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyMap()
    )

    private val isLoading = MutableStateFlow(false)


    val state = combine(items, isLoading){ items, loading ->
        ItemState(loading, items)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ItemState(false, null)
    )


    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                itemRepository.fetchItems()
            }catch(e: Exception){
                println(e.message)
            }finally {
                isLoading.value = false
            }
        }
    }
}

data class ItemState(
    val isLoading: Boolean = false,
    val items: Map<Int, List<Item>>? = null
)