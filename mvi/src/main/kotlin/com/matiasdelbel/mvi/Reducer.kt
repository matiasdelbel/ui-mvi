package com.matiasdelbel.mvi

fun interface Reducer<State : Any, Intent : Any> {

    operator fun invoke(state: State, intent: Intent): State

}