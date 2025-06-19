package com.matiasdelbel.mvi

interface IntentScope<State : Any, Intent : Any> {

    val state: State

    fun dispatch(intent: Intent)
}
