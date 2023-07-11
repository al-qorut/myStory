package com.alqorut.mystory.viewmodels

import androidx.lifecycle.ViewModel
import com.alqorut.mystory.api.response.errorResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.interfaces.StoryListener
import com.alqorut.mystory.models.Story
import com.alqorut.mystory.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val repoStory: RepoStory) : ViewModel() {
    private val disposables = CompositeDisposable()
    var listener : StoryListener?=null

    fun uploadImage(file: MultipartBody.Part, desc : RequestBody){
        listener?.onMulai()
        val disposable = repoStory.uploadImage(file, desc)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                listener?.onUploadResponse(it)
            },{
                listener?.onError(it.toString())
            })
        disposables.add(disposable!!)
    }

    fun tambahStory(token:String, file: MultipartBody.Part, desc : RequestBody, lat : RequestBody, lon : RequestBody){
        listener?.onMulai()
        val disposable = repoStory.tambahStory(token, file, desc, lat, lon)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                listener?.onUploadResponse(it)
            },{
                listener?.onError(it.toString())
            })
        disposables.add(disposable!!)
    }

    fun loadStory(token:String, page : Int, size : Int, loc : Int){
        listener?.onMulai()
        val disposable = repoStory.loadStory(token, page, size, loc)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                listener?.onLoadStories(it)
            },{
                listener?.onError(it.toString())
            })
        disposables.add(disposable!!)
    }
}