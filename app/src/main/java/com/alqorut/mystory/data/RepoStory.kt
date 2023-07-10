package com.alqorut.mystory.data

import com.alqorut.mystory.api.ApiService
import com.alqorut.mystory.api.response.LoginResponse
import com.alqorut.mystory.api.response.RegisterResponse
import com.alqorut.mystory.api.response.StoryResponse
import com.alqorut.mystory.api.response.UploadResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepoStory constructor(private val remote : ApiService ){
    /*
        private val local = dbStory.getIntance()
        private val remote = ApiConfig().getApiService(config.token.toString())
        fun addStory(story: Story) = Completable.fromAction {local?.story()?.addStory(story)}
        fun getStory() : Flowable<MutableList<Story>>? = local?.story()?.getStory()
        fun addUser(user: User) = Completable.fromAction {  local?.user()?.addUser(user) }
        fun getUser(email: String, pass:String) = local?.user()?.getUser(email, pass)
    */

    fun register(nama:String, email: String, pass: String) : Observable<Result<RegisterResponse>> = remote.register(nama,email, pass)
    fun login(email: String, pass: String) : Observable<LoginResponse> = remote.login(email, pass)
    fun uploadImage(file: MultipartBody.Part, desc : RequestBody) = remote.uploadImage(file, desc)
    fun loadStory(token: String, page :Int, size:Int, loc:Int) :  Observable<StoryResponse> = remote.getStories(token)
    fun tambahStory(token: String, file: MultipartBody.Part, desc :RequestBody, lat:RequestBody, lon:RequestBody) :  Observable<Result<UploadResponse>> = remote.addStory(token,file,desc,lat,lon)





}