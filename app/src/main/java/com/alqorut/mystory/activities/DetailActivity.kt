package com.alqorut.mystory.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.alqorut.mystory.api.response.ListStoryItem
import com.alqorut.mystory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var item : ListStoryItem?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item = intent.extras?.getParcelable("detail", ListStoryItem::class.java)
        }else{
            item = intent.extras?.getParcelable("detail")
        }
        setupData()
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

    private fun setupData(){
        binding.apply {
            itemDetailName.text = item?.name.toString()
            itemDescription.text = item?.description.toString()
            itemDetailDate.text = item?.createdAt.toString()
            var options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CenterCrop(), RoundedCorners(10))

            Glide.with(this@DetailActivity)
                .load(item?.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options)
                .into(itemDetailPhoto)
        }
    }

}