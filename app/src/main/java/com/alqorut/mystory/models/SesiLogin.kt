package com.alqorut.mystory.models

data class SesiLogin(
    val name: String,
    val email: String,
    val password: String,
    val token : String,
    val isLogin: Boolean
)