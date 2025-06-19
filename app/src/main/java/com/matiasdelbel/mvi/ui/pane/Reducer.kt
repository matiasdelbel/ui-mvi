package com.matiasdelbel.mvi.ui.pane

import com.matiasdelbel.mvi.Reducer

val DefaultReducer = Reducer<UiState, Intent> { state, intent ->
    when (intent) {

        Intent.AddTodoItem -> state

        is Intent.AddTodoItemSucceeded -> state.copy(
            isAdding = false,
            items = state.items + intent.newTodo,
            itemDraft = InputFieldValue(value = "", error = null),
        )

        is Intent.AddTodoItemFailed -> state.copy(
            isAdding = false,
            snackbarError = "Cannot add item"
        )

        is Intent.DeleteTodoItem -> state

        is Intent.DeleteTodoItemSucceeded -> state.copy(
            items = state.items.filterNot { it.id == intent.id }
        )

        is Intent.DeleteTodoItemFailed -> state.copy(
            snackbarError = "Cannot delete item"
        )

        is Intent.LoadItems -> state.copy(
            isLoading = true,
            error = null
        )

        is Intent.LoadItemsSucceeded -> state.copy(
            isLoading = false,
            items = intent.todos
        )

        is Intent.LoadItemsFailed -> state.copy(
            isLoading = false,
            error = intent.throwable
        )

        is Intent.UpdateInput -> state.copy(
            itemDraft = InputFieldValue(
                value = intent.text,
                error = null
            )
        )
    }
}
