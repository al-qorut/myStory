package com.alqorut.mystory.interfaces

import com.alqorut.mystory.api.response.RegisterResponse
import com.alqorut.mystory.api.response.StoryResponse
import com.alqorut.mystory.api.response.UploadResponse
import com.alqorut.mystory.api.response.errorResponse
import com.alqorut.mystory.models.Story

interface StoryListener {
    fun onMulai(){}
    fun onSukses(){}
    fun onError(msg:String){}
    fun onLoadStory(story:MutableList<Story>){}
    fun onUploadResponse(response: Result<UploadResponse>){}
    fun onLoadStories(story : StoryResponse){}
    fun onErrorResponse(eror: errorResponse){}
}