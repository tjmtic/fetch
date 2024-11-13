package com.example.fetchapplication.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fetchapplication.feature.item.ItemViewModel

@Composable
fun ItemScreen(
    viewModel : ItemViewModel = hiltViewModel()
){
    val itemState by viewModel.state.collectAsStateWithLifecycle()
    //Sorting "when displaying" (I.e. on UI thread)
    val sortedItems by remember {
        derivedStateOf {
            //Sort by listId -- "listId" is the Key in this Map
            itemState.items?.toSortedMap()?.mapValues { entry ->
                //Then, sort by Name -- "name" is the Value in this Map
                entry.value.sortedBy { it.name }
            } ?: emptyMap()
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {

        //Simple Loading Indication
        when (itemState.isLoading) {
            true -> {
                CircularProgressIndicator()
            }
            else -> {}
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            sortedItems.forEach { (listId, items) ->
                item {
                    Text(text = listId.toString())
                }
                items(items) { item ->
                    ListItem(data = item)
                }
            }
        }
    }
}