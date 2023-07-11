package com.alqorut.mystory.viewmodels

import androidx.lifecycle.ViewModel
import com.alqorut.mystory.api.response.errorResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.interfaces.SignUpListener
import com.alqorut.mystory.models.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SignupViewModel constructor(private val repoStory: RepoStory) : ViewModel() {
    private val disposables = CompositeDisposable()
    var listener : SignUpListener?=null


    fun register(nama:String, email: String, pass: String){
        listener?.onMulai()
        val disposable = repoStory.register(nama, email, pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listener?.onRegisterResponse(it)
            },{
                when(it){
                    is HttpException ->{
                        val jsonInString = it.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, errorResponse::class.java)
                        val errorMessage = errorBody.message
                        listener?.onError(errorMessage.toString())
                    }
                    is SocketTimeoutException ->{
                        listener?.onError(it.message.toString())
                    }
                    is IOException ->{
                        listener?.onError(it.message.toString())
                    }
                    else ->{
                        listener?.onError(it.message.toString())
                    }
                }
            })
        disposables.add(disposable)
    }
}