package com.alqorut.mystory.api

import android.content.Context
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.helpers.Config

object Injection {
    fun provideRepository(context: Context): RepoStory {
        val pref = Config.newInstance(context)
        val apiService = ApiConfig().getApiService(pref.token.toString())
        return RepoStory(apiService)
    }
}