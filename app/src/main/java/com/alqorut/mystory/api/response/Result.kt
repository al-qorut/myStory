package com.alqorut.mystory.api.response

sealed class Result<out R> {
    data class Success<out T>(val data: T?) : Result<T>()
    data class Error(val msg: String) : Result<Nothing>()
}