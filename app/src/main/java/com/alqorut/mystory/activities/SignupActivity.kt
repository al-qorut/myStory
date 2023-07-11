package com.alqorut.mystory.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alqorut.mystory.R
import com.alqorut.mystory.api.ApiConfig
import com.alqorut.mystory.api.response.RegisterResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.databinding.ActivitySignupBinding
import com.alqorut.mystory.helpers.Config
import com.alqorut.mystory.helpers.isProgress
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.helpers.toas
import com.alqorut.mystory.interfaces.SignUpListener
import com.alqorut.mystory.models.User
import com.alqorut.mystory.viewmodels.SignupViewModel
import com.alqorut.mystory.viewmodels.ViewModelFactory


class SignupActivity : AppCompatActivity(), SignUpListener {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var repoStory: RepoStory
    private lateinit var config : Config

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        config = Config(this)
        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
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
        val apiService = ApiConfig().getApiService(config.token.toString())
        repoStory = RepoStory(apiService)
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repoStory)
        ).get(SignupViewModel::class.java)
        signupViewModel.listener = this
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.masukan_nama)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error =  getString(R.string.masukan_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error =  getString(R.string.masukan_pass)
                }
                else -> {
                  //  signupViewModel.saveUser(User(email, name , password))
                        signupViewModel.register(name, email, password)
                }
            }
        }
    }
    override fun onMulai(){
        isProgress(this@SignupActivity, true)
    }

    override fun onRegisterResponse(response: Result<RegisterResponse>) {
        isProgress(this,false)
        response.apply {
            when {
                isSuccess -> {
                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle(getString(R.string.horee))
                        setMessage(getString(R.string.account_sukses))
                        setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }

                isFailure -> {
                    toas(this@SignupActivity, response.exceptionOrNull()?.message.toString())
                }
            }
        }

    }

    override fun onError(msg: String) {
        toas(this, msg)
        isProgress(this,false)
    }

    override fun onAddUser() {
        super.onAddUser()
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.horee))
            setMessage(getString(R.string.account_sukses))
            setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                finish()
            }
            create()
            show()
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 500
        }.start()
    }
}