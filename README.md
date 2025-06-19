# ui-mvi
A simple TODO app built using **Jetpack Compose** and a clean **MVI (Model–View–Intent)** architecture. 
This project demonstrates how to structure a scalable Android app using unidirectional data flow, pure reducers, and modular middlewares.

## Goal
Build a basic TODO list app where users can:
- ✅ View pending tasks
- ➕ Add new tasks
- ❌ Delete existing tasks

All implemented using a modular, testable MVI architecture.

## Architecture Overview
The project follows a **feature-first modular design** using a clean MVI approach:
```
feature/
├── Intent.kt // User actions
├── UiState.kt // Current UI representation
├── Reducer.kt // Pure state transformation logic
├── ViewModel.kt // Coordinates everything
├── Middlewares.kt // Side effects (e.g. data fetching)
├── Module.kt // Dependency injection
├── Navigation.kt // Feature navigation
├── Pane.kt // UI Composable
```

> ⚠️ **Note:** Class names like `Intent` and `UiState` are kept generic in this example for simplicity, but in a production app you should **scope them to the feature**, e.g. `TodoIntent`, `TodoUiState`.

## Intents

The `Intent` sealed interface defines all the possible user actions and system events that can mutate the UI state:

```kotlin
sealed interface Intent {
    data object LoadItems : Intent
    data class AddItem(val value: String) : Intent
    data class DeleteItem(val id: String) : Intent
}
```

## UiState
The single source of truth for the screen:
```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val items: List<PendingItem> = emptyList(),
    val error: String? = null
)
```

## Reducer
The Reducer is a pure function that takes the current state and an Intent, and returns a new state:
```kotlin
class Reducer : MviReducer<Intent, UiState> {
    override fun reduce(currentState: UiState, intent: Intent): UiState {
        return when (intent) {
            is Intent.LoadItems -> currentState.copy(isLoading = true)
            is Intent.AddItem -> currentState.copy(
                items = currentState.items + PendingItem(UUID.randomUUID().toString(), intent.value)
            )
            is Intent.DeleteItem -> currentState.copy(
                items = currentState.items.filterNot { it.id == intent.id }
            )
        }
    }
}
```

✅ Using a sealed interface with when ensures exhaustive coverage: if you add a new intent, the compiler forces you to handle it here.

## Middleware
Handles side effects like repository calls. Example:
```kotlin
class LoadItemsMiddleware(
    private val repository: PendingRepository
) : Middleware<Intent, UiState> {
    override suspend fun process(
        intent: Intent,
        state: UiState,
        dispatch: Dispatch<Intent>
    ) {
        if (intent is Intent.LoadItems) {
            val result = repository.getPendingItems()
            result.onSuccess { items ->
                dispatch(InternalIntent.LoadItemsSuccess(items))
            }.onFailure {
                dispatch(InternalIntent.LoadItemsError(it.message ?: "Unknown error"))
            }
        }
    }
}
```

## ViewModel
Coordinates the reducer, middleware, and exposes state to the UI:
```kotlin
class TodoViewModel @Inject constructor(
    reducer: Reducer,
    middlewares: Set<@JvmSuppressWildcards Middleware<Intent, UiState>>
) : MviViewModel<Intent, UiState>(
    initialState = UiState(),
    reducer = reducer,
    middlewares = middlewares
)
```

## UI (Pane)
Composable that reacts to state and dispatches intents:
```kotlin
fun TodoPane(
    state: UiState,
    onTextChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onDeleteClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
)
```

## Contributing
PRs are welcome! Feel free to open issues or contribute new features.

## License
MIT License
