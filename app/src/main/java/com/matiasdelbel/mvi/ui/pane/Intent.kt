package com.matiasdelbel.mvi.ui.pane

import com.matiasdelbel.mvi.model.TodoItem

sealed interface Intent {

    object AddTodoItem : Intent
    data class AddTodoItemSucceeded(val newTodo: TodoItem) : Intent
    data class AddTodoItemFailed(val error: Throwable) : Intent

    data class DeleteTodoItem(val id: String) : Intent
    data class DeleteTodoItemSucceeded(val id: String) : Intent
    data class DeleteTodoItemFailed(val error: Throwable) : Intent

    object LoadItems : Intent
    data class LoadItemsSucceeded(val todos: List<TodoItem>) : Intent
    data class LoadItemsFailed(val throwable: Throwable) : Intent

    data class UpdateInput(val text: String) : Intent
}
