package com.devapp.fr.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityFullScreenImageBinding
import com.devapp.fr.util.storages.SharedPreferencesHelper

class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("url").let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_broken_image)
                .into(binding.image)
            val isDarkMode = SharedPreferencesHelper(this).readDarkMode()
            isDarkMode.let {
                if (it) binding.root.setBackgroundColor(resources.getColor(R.color.background_dark_mode))
                else binding.root.setBackgroundColor(resources.getColor(R.color.background_light_mode))
            }

        }
    }
}