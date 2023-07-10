package com.alqorut.mystory.api.response

import com.google.gson.annotations.SerializedName

data class errorResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)
