package com.devapp.fr.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityFullScreenImageBinding
import com.devapp.fr.util.storages.SharedPreferencesHelper
import android.graphics.BitmapFactory
import com.devapp.fr.util.GlideApp
import java.io.FileInputStream
import java.lang.Exception


class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getStringExtra("image").let {
            try {
                val ip: FileInputStream = openFileInput(it)
                val bmp = BitmapFactory.decodeStream(ip)
                ip.close()
                binding.image.setImageBitmap(bmp)
            } catch (e: Exception) {
                GlideApp.loadImage(
                    intent.getStringExtra("url").toString(),
                    binding.image,
                    this
                )
            }

        }
        val isDarkMode = SharedPreferencesHelper(this).readDarkMode()
        isDarkMode.let {
            if (it) binding.root.setBackgroundColor(resources.getColor(R.color.background_dark_mode))
            else binding.root.setBackgroundColor(resources.getColor(R.color.background_light_mode))
        }
    }
}