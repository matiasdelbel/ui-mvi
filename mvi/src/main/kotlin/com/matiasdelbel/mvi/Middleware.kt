package com.matiasdelbel.mvi

interface Middleware<State : Any, ParentIntent : Any, Intent : Any> {

    val intentClass: Class<Intent>

    suspend fun process(intent: Intent, scope: IntentScope<State, ParentIntent>)
}

@Suppress("UNCHECKED_CAST")
internal suspend fun <State : Any, ParentIntent : Any, Intent : Any> Middleware<State, ParentIntent, Intent>.process(
    intent: Any,
    scope: IntentScope<State, ParentIntent>
) {
    if (intentClass == intent::class.java) {
        process(intent as Intent, scope)
    }
}

inline fun <State : Any, reified ParentIntent : Any, reified Intent : Any> Middleware(
    noinline emit: suspend IntentScope<State, ParentIntent>.(intent: Intent) -> Unit
) = object : Middleware<State, ParentIntent, Intent> {

    override val intentClass: Class<Intent>
        get() = Intent::class.java

    override suspend fun process(intent: Intent, scope: IntentScope<State, ParentIntent>) {
        scope.emit(intent)
    }
}
