package com.matiasdelbel.mvi.ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.matiasdelbel.mvi.ui.theme.spacers

@Composable
fun TodoPane(
    state: UiState,
    onTextChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onDeleteClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarError = state.snackbarError
    if (snackbarError != null) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(snackbarError)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.error != null -> Text("Error: ${state.error.message}")
                    state.items.isEmpty() -> Text("No todos yet")
                    else -> LazyColumn {
                        items(state.items) { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item.text)
                                IconButton(onClick = { onDeleteClicked(item.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    }
                }
            }

            Column {
                OutlinedTextField(
                    value = state.itemDraft.value,
                    onValueChange = onTextChanged,
                    label = { Text("New Todo") },
                    isError = state.itemDraft.error != null,
                    modifier = Modifier.fillMaxWidth()
                )
                state.itemDraft.error?.let {
                    // TODO Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onAddClicked,
                    enabled = !state.isAdding,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isAdding) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Add")
                    }
                }
            }
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
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(weight = 0.5f)
        )

        TextButton(
            onClick = { onDelete(item) },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
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
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        HorizontalDivider(
            modifier = Modifier.padding(
                top = MaterialTheme.spacers.md,
                bottom = MaterialTheme.spacers.sm
            )
        )

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacers.sm),
        ) {
            DecoratedTextField(
                isError = !draft.isValid,
                label = { TextFieldLabel(text = stringResource(R.string.todo_items_draft_label)) },
                textField = {
                    TextField(
                        value = draft.value,
                        onValueChange = { newValue -> onDraftTextChanged(newValue) },
                        placeholder = { TextFieldHint(text = stringResource(R.string.todo_items_draft_hint)) },
                        isError = !draft.isValid,
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
            TextButton (
                onClick = { onSave() },
                enabled = draft.isValid,
                content = { Text(text = stringResource(R.string.todo_items_draft_action_add)) },
                modifier = Modifier.weight(0.3f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun TodoItemsPanePreview() {
//    AppTheme {
//        TodoItemsPane(
//            items = listOf(
//                TodoItem(id = 1, text = "Buy milk"),
//                TodoItem(id = 2, text = "Buy eggs"),
//                TodoItem(id = 3, text = "Buy bread"),
//                TodoItem(id = 4, text = "Buy cheese"),
//                TodoItem(id = 5, text = "Buy butter"),
//            ),
//            draft = InputFieldValue(value = ""),
//            onDraftTextChanged = {},
//            onSave = {},
//            onDelete = {},
//        )
//    }
}
