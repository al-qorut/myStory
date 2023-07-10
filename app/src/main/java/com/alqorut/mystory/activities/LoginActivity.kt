package com.alqorut.mystory.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import com.alqorut.mystory.R
import com.alqorut.mystory.api.ApiConfig
import com.alqorut.mystory.api.ApiService
import com.alqorut.mystory.api.response.LoginResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.data.SesiPreference
import com.alqorut.mystory.databinding.ActivityLoginBinding
import com.alqorut.mystory.helpers.Config
import com.alqorut.mystory.helpers.dataStore
import com.alqorut.mystory.helpers.isProgress
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.helpers.toas
import com.alqorut.mystory.interfaces.LoginListener
import com.alqorut.mystory.models.User
import com.alqorut.mystory.viewmodels.LoginViewModel
import com.alqorut.mystory.viewmodels.ViewModelFactory


class LoginActivity : AppCompatActivity(), LoginListener {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var content: View
    private lateinit var config: Config
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        config = Config(this)
        setupView()
        setupViewModel()
        setupAction()
        content = findViewById(android.R.id.content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            content.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean =
                    when {
                        loginViewModel.mockDataLoading() -> {
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        }
                        else -> false
                    }
            })
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_X,
                    0f,
                    splashScreenView.width.toFloat()
                ).apply {
                    duration = 1000
                    doOnEnd {
                        splashScreenView.remove()
                    }
                }.also {
                    it.start()
                }
            }
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
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        apiService = ApiConfig().getApiService(config.token.toString())
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(RepoStory(apiService))
        ).get(LoginViewModel::class.java)
        loginViewModel.listener = this

        if(config.userLogin){
            runMain()
        }
    }

    override fun onMulai() {
        super.onMulai()
        isProgress(this, true)
    }

    override fun onGagal(info: String) {
        toas(this,getString(R.string.login_gagal)+" $info")
        isProgress(this, false)
    }

    override fun onLoginResponse(response: LoginResponse) {
        isProgress(this,false)
        if(!response.error!!){
            config.userLogin = true
            config.token = "Bearer ".plus(response.loginResult?.token.toString())
            runMain()
        }else{
            toas(this, response.message.toString())
        }
    }
    private fun runMain(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
    override fun onLoadUser(user: List<User>) {

    }


    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.masukan_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.masukan_pass)
                }
                else -> {
                    loginViewModel.login(email, password)
                }
            }
        }
    }

}