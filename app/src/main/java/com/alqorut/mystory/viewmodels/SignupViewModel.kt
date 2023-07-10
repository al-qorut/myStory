package com.alqorut.mystory.viewmodels

import androidx.lifecycle.ViewModel
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.interfaces.SignUpListener
import com.alqorut.mystory.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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
                listener?.onError(it.toString())
            })
        disposables.add(disposable)
    }
}