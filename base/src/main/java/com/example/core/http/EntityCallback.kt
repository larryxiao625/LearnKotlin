package com.example.core.http

interface EntityCallback<T> {
    fun <T> onSuccess(entity: T)
    fun onFailure(message: String?)
}