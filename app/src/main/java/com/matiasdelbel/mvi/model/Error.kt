package com.matiasdelbel.mvi.model

sealed interface CreatePendingItemError {

    object EmptyName : CreatePendingItemError

    object NameTooShort : CreatePendingItemError
}