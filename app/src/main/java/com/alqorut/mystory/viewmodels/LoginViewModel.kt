package com.alqorut.mystory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.interfaces.LoginListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                listener?.onGagal(it.toString())
            })
        disposables.add(disposable!!)
    }

}