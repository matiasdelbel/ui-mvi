package com.matiasdelbel.mvi.ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matiasdelbel.mvi.ui.component.DecoratedTextField
import com.matiasdelbel.mvi.ui.component.TextFieldError
import com.matiasdelbel.mvi.ui.component.TextFieldHint
import com.matiasdelbel.mvi.ui.component.TextFieldLabel
import com.matiasdelbel.mvi.R
import com.matiasdelbel.mvi.model.CreatePendingItemError
import com.matiasdelbel.mvi.model.TodoItem
import com.matiasdelbel.mvi.ui.theme.AppTheme
import com.matiasdelbel.mvi.ui.theme.spacers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPane(
    state: UiState,
    onTextChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onDeleteClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarError = state.snackbarError
    if (snackbarError != null) {
        LaunchedEffect(Unit) { snackbarHostState.showSnackbar(snackbarError) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacers.md)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(text = state.error.message.orEmpty())
                state.items.isEmpty() -> Text(text = stringResource(R.string.todo_items_empty_list))
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stickyHeader {
                        Text(
                            text = stringResource(R.string.todo_items_headers),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    items(state.items) { item -> Item(item = item, onDelete = onDeleteClicked) }
                }
            }

            Footer(
                draft = state.itemDraft,
                onDraftTextChanged = onTextChanged,
                onSave = onAddClicked,
                enabled = !state.isAdding,
                loading = state.isAdding,
            )
        }
    }
}

@Composable
private fun Item(
    item: TodoItem,
    onDelete: (item: TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.text,
            modifier = Modifier.weight(weight = 0.8f)
        )
        TextButton(
            onClick = { onDelete(item) },
            content = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.todo_items_item_action_delete),
                )
            }
        )
    }
}

@Composable
private fun Footer(
    draft: InputFieldValue<CreatePendingItemError>,
    onDraftTextChanged: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.sm),
        modifier = modifier
    ) {
        HorizontalDivider(modifier = Modifier.padding(vertical = MaterialTheme.spacers.md))

        DecoratedTextField(
            modifier = Modifier.fillMaxWidth(),
            isError = !draft.isValid,
            label = { TextFieldLabel(text = stringResource(R.string.todo_items_draft_label)) },
            textField = {
                TextField(
                    value = draft.value,
                    onValueChange = { newValue -> onDraftTextChanged(newValue) },
                    placeholder = { TextFieldHint(text = stringResource(R.string.todo_items_draft_hint)) },
                    isError = !draft.isValid,
                    enabled = enabled && !loading,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            },
            error = {
                val error = draft.error!!
                val errorText = when (error) {
                    CreatePendingItemError.EmptyName -> stringResource(R.string.todo_items_draft_error_empty_name)
                    CreatePendingItemError.NameTooShort -> stringResource(R.string.todo_items_draft_error_name_too_short)
                }

                TextFieldError(text = errorText)
            },
        )

        Button(
            onClick = { onSave() },
            enabled = enabled && draft.isValid,
            modifier = Modifier.fillMaxWidth(),
            content = {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.todo_items_draft_action_add))
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun TodoItemsPanePreview() {
    AppTheme {
        TodoPane(
            state = UiState(
                items = listOf(
                    TodoItem(id = "1", text = "Buy milk"),
                    TodoItem(id = "2", text = "Buy eggs"),
                    TodoItem(id = "3", text = "Buy bread"),
                    TodoItem(id = "4", text = "Buy cheese"),
                    TodoItem(id = "5", text = "Buy butter"),
                ),
                itemDraft = InputFieldValue(value = "Buy cheesecake"),
            ),
            onTextChanged = {},
            onAddClicked = {},
            onDeleteClicked = {},
        )
    }
}
