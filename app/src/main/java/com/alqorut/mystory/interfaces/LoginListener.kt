package com.alqorut.mystory.interfaces

import com.alqorut.mystory.api.response.LoginResponse
import com.alqorut.mystory.models.User

interface LoginListener {
    fun onMulai(){}
    fun onGagal(info:String){}
    fun onLoadUser(user: List<User>){}
    fun onLoginResponse(response: LoginResponse){}
 }