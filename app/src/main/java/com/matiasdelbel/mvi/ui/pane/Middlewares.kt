package com.matiasdelbel.mvi.ui.pane

import com.matiasdelbel.mvi.Middleware
import com.matiasdelbel.mvi.data.TodoRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

fun addTodoMiddleware(repository: TodoRepository) = Middleware<UiState, Intent, Intent.AddTodoItem> {
    val text = state.itemDraft.value.trim()

    try {
        val item = repository.addItem(text)
        dispatch(intent = Intent.AddTodoItemSucceeded(item))
    } catch (exception: Exception) {
        dispatch(intent = Intent.AddTodoItemFailed(error = exception))
    }
}

fun deleteTodoMiddleware(repository: TodoRepository) = Middleware<UiState, Intent, Intent.DeleteTodoItem> { intent ->
    try {
        repository.deleteItem(id = intent.id)
        dispatch(intent = Intent.DeleteTodoItemSucceeded(id = intent.id))
    } catch (exception: Exception) {
        dispatch(intent = Intent.DeleteTodoItemFailed(error = exception))
    }
}

fun loadTodosMiddleware(repository: TodoRepository) = Middleware<UiState, Intent, Intent.LoadItems> {
    repository
        .getAll()
        .onEach { dispatch(intent = Intent.LoadItemsSucceeded(it)) }
        .catch { dispatch(intent = Intent.LoadItemsFailed(it)) }
        .collect()
}
