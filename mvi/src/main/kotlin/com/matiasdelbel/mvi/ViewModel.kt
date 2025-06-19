package com.matiasdelbel.mvi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class ViewModel<State: Any, ParentIntent: Any>(
    initialUiState: State,
    private val reducer: Reducer<State, ParentIntent>,
    private val middlewares: Set<@JvmSuppressWildcards Middleware<State, ParentIntent, *>>
) : androidx.lifecycle.ViewModel() {

    private val _state = MutableStateFlow(value = initialUiState)
    val state: StateFlow<State> = _state.asStateFlow()

    fun process(intent: ParentIntent) {
        val intentScope = object: IntentScope<State, ParentIntent> {

            override val state = _state.value

            override fun dispatch(intent: ParentIntent) { process(intent) }
        }

        viewModelScope.launch {
            middlewares
                .filter { it.intentClass == intent::class.java }
                .onEach { middleware -> middleware.process(intent = intent, scope = intentScope) }
                .ifEmpty { _state.value = reducer(_state.value, intent) }
        }
    }
}
