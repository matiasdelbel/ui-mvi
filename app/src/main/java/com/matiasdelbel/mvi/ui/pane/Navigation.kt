package com.matiasdelbel.mvi.ui.pane

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.todoPane() {
    composable(route = TodoPaneRoute) {
        val viewModel = hiltViewModel<TodoViewModel>()
        val state by viewModel.state.collectAsState()

        TodoPane(
            state = state,
            onTextChanged = { viewModel.process(Intent.UpdateInput(it)) },
            onAddClicked = { viewModel.process(Intent.AddTodoItem) },
            onDeleteClicked = { viewModel.process(Intent.DeleteTodoItem(it.id)) },
        )
    }
}

const val TodoPaneRoute = "items"
