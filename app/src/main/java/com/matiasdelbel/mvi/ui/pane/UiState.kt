package com.matiasdelbel.mvi.ui.pane

import com.matiasdelbel.mvi.model.CreatePendingItemError
import com.matiasdelbel.mvi.model.TodoItem

data class UiState(
    val items: List<TodoItem> = emptyList(),
    val itemDraft: InputFieldValue<CreatePendingItemError> = InputFieldValue(),
    val isAdding: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val snackbarError: String? = null
)

data class InputFieldValue<E : Any>(
    val value: String = "",
    val error: E? = null,
) {
    val isValid: Boolean get() = error == null
}
