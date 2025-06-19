package com.matiasdelbel.mvi.ui.pane

import com.matiasdelbel.mvi.Middleware
import com.matiasdelbel.mvi.Reducer
import com.matiasdelbel.mvi.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    reducer: Reducer<UiState, Intent>,
    @TodoMiddleware middlewares: Set<@JvmSuppressWildcards Middleware<UiState, Intent, *>>
) : ViewModel<UiState, Intent>(
    reducer = reducer,
    initialUiState = UiState(),
    middlewares = middlewares
) {

    init {
        process(intent = Intent.LoadItems)
    }
}
