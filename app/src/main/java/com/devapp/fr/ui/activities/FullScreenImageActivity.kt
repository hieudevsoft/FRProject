package com.devapp.fr.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityFullScreenImageBinding
import com.devapp.fr.util.storages.SharedPreferencesHelper
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.devapp.fr.util.GlideApp
import java.io.FileInputStream
import java.lang.Exception


class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInsetsWindow()
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
    }

    private fun setInsetsWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            WindowInsetsCompat.CONSUMED
        }
    }

}