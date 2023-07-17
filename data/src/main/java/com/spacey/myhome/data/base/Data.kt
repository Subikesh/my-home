package com.spacey.myhome.data.base

sealed class Data<out T> {
    data class Success<T>(val data: T) : Data<T>()
    data class Error(val exception: Throwable? = null, val errorMessage: String? = null) :
        Data<Nothing>()

    fun <R> transform(block: (T) -> R): Data<R> = when (this) {
        is Success -> Success(block(data))
        is Error -> this
    }
}