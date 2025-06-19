package com.matiasdelbel.mvi.data

import com.matiasdelbel.mvi.di.IoDispatcher
import com.matiasdelbel.mvi.model.TodoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class TodoRepository @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    private val items = MutableStateFlow<List<TodoItem>>(value = emptyList())

    fun getAll(): Flow<List<TodoItem>> = items

    suspend fun addItem(text: String): TodoItem = withContext(dispatcher) {
        delay(timeMillis = 1000)
        val item = TodoItem(
            id = UUID.randomUUID().toString(),
            text = text
        )

        items.value = items.value + item
        item
    }

    suspend fun deleteItem(id: String) {
        withContext(dispatcher) {
            delay(timeMillis = 1000)
            val updatedItemList = items
                .value
                .toMutableSet()
                .apply { removeAll { it.id == id } }
                .toList()

            items.value = updatedItemList
        }
    }
}
