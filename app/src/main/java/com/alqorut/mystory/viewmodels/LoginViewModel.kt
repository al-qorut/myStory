package com.alqorut.mystory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alqorut.mystory.api.response.errorResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.interfaces.LoginListener
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class LoginViewModel(private val repoStory: RepoStory) : ViewModel() {
    private val disposables = CompositeDisposable()
    var listener : LoginListener?=null
    private var dataLoaded = false

    fun mockDataLoading(): Boolean {
        viewModelScope.launch {
            delay(5000)
            dataLoaded = true
        }
        return dataLoaded
    }


    fun login(user: String, pass :String) {
        listener?.onMulai()
        val disposable = repoStory.login(user, pass)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                listener?.onLoginResponse(it)
            },{
                when(it){
                    is HttpException ->{
                        val jsonInString = it.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, errorResponse::class.java)
                        val errorMessage = errorBody.message
                        listener?.onGagal(errorMessage.toString())
                    }
                    is SocketTimeoutException->{
                        listener?.onGagal(it.message.toString())
                    }
                    is IOException ->{
                        listener?.onGagal(it.message.toString())
                    }
                    else ->{
                        listener?.onGagal(it.message.toString())
                    }
                }
            })
        disposables.add(disposable!!)
    }

}