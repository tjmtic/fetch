package com.example.fetchapplication.ui.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.fetchapplication.data.Item

@Composable
fun ListItem(data: Item) {
    Text(text = data.name ?: "")
}