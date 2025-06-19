package com.matiasdelbel.mvi.ui.pane

import com.matiasdelbel.mvi.Middleware
import com.matiasdelbel.mvi.Reducer
import com.matiasdelbel.mvi.data.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object TodoModule {

    @Provides
    fun provideReducer(): Reducer<UiState, Intent> = DefaultReducer

    @Provides
    @IntoSet
    @TodoMiddleware
    fun provideLoadMiddleware(repo: TodoRepository): Middleware<UiState, Intent, *> = loadTodosMiddleware(repo)

    @Provides
    @IntoSet
    @TodoMiddleware
    fun provideAddMiddleware(repo: TodoRepository): Middleware<UiState, Intent, *> = addTodoMiddleware(repo)

    @Provides
    @IntoSet
    @TodoMiddleware
    fun provideDeleteMiddleware(repo: TodoRepository): Middleware<UiState, Intent, *> = deleteTodoMiddleware(repo)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TodoMiddleware
