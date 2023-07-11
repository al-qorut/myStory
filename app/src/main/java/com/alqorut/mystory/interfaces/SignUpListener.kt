package com.alqorut.mystory.interfaces

import com.alqorut.mystory.api.response.RegisterResponse

interface SignUpListener {
    fun onMulai(){}
    fun onAddUser(){}
    fun onError(msg:String){}
    fun onRegisterResponse(response: Result<RegisterResponse>){}
}