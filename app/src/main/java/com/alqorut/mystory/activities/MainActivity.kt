package com.alqorut.mystory.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alqorut.mystory.R
import com.alqorut.mystory.adapters.StoryAdapter
import com.alqorut.mystory.api.ApiConfig
import com.alqorut.mystory.api.ApiService
import com.alqorut.mystory.api.response.ListStoryItem
import com.alqorut.mystory.api.response.StoryResponse
import com.alqorut.mystory.api.response.errorResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.databinding.ActivityMainBinding
import com.alqorut.mystory.helpers.Config
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.helpers.toas
import com.alqorut.mystory.interfaces.StoryListener
import com.alqorut.mystory.models.Story
import com.alqorut.mystory.viewmodels.MainViewModel
import com.alqorut.mystory.viewmodels.ViewModelFactory


class MainActivity : AppCompatActivity(), StoryListener {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var config: Config
    private var listStory : List<ListStoryItem> = listOf()
    private lateinit var repo: RepoStory
    private lateinit var adapterX: StoryAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupView()
        setupAction()
    }


    private fun setupAction() {
        binding.logout.setOnClickListener {
            logout()
        }
        binding.addStory.setOnClickListener {
            startActivity(Intent(this,TambahActivity::class.java))
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.isShowing()
        binding.liststory.apply {
            layoutManager =  LinearLayoutManager(this@MainActivity)
            adapter = adapterX
        }
    }

    override fun onMulai() {
        super.onMulai()
        binding.progressBar.show()
    }
    override fun onResume() {
        super.onResume()
        adapterX.notifyDataSetChanged()
    }

    override fun onError(msg: String) {
        super.onError(msg)
        binding.progressBar.hide()
        toas(this, msg)
        msg("tah erer ".plus(msg))
    }

    override fun onErrorResponse(eror: errorResponse) {
        super.onErrorResponse(eror)
        binding.progressBar.hide()
        toas(this, eror.message!!)
        msg(eror.message)
    }
    private fun setupViewModel() {
        config = Config(this)
        apiService = ApiConfig().getApiService(config.token.toString())
        repo = RepoStory(apiService)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repo)
        ).get(MainViewModel::class.java)
        mainViewModel.listener = this
        config = Config(this)
        mainViewModel.loadStory(config.token.toString(),1,0,0)
        adapterX = StoryAdapter(this,listStory)
    }

    override fun onLoadStories(story: StoryResponse) {
        super.onLoadStories(story)
        binding.progressBar.hide()
        if(!story.error) {
            listStory = story.listStory
            adapterX.setItems(listStory)
        }else{
            toas(this,story.message)
        }
        msg("ada eror ".plus(story.message))
    }



    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        config.userLogin = false
        config.token = ""
        finish()
    }


}