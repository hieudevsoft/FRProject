package com.devapp.fr.ui.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.devapp.fr.adapters.ViewImagePartnerAdapter
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.ActivityViewPartnerBinding
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.UiHelper.sendImageToFullScreenImageActivity
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import java.io.FileInputStream
import java.lang.Exception

class ViewPartnerActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityViewPartnerBinding
    private lateinit var user:UserProfile
    private lateinit var imageProfileImagesAdapter: ViewImagePartnerAdapter
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewPartnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = intent.getSerializableExtra("data") as UserProfile
        setImageFullScreen()
        setRecyclerViewImage()
        animateBottomCard()
        binding.apply {
            lyIcBack.setOnClickWithAnimationListener {
                finish()
            }
        }
    }

    private fun animateBottomCard() {
        binding.apply {
            cardBottom.alpha = 0f
            cardBottom.translationY = 2000f
            cardBottom.animate().translationY(0f).setDuration(2000L).start()
            cardBottom.animate().alpha(1f).setDuration(500L).setStartDelay(1000L).start()
        }
    }


    private fun setRecyclerViewImage() {
        binding.rcImage.apply {
            imageProfileImagesAdapter = ViewImagePartnerAdapter(this@ViewPartnerActivity)
            itemAnimator = SlideInLeftAnimator(OvershootInterpolator(1f))
            layoutManager = LinearLayoutManager(this@ViewPartnerActivity,HORIZONTAL,false)
            adapter = imageProfileImagesAdapter
            imageProfileImagesAdapter.submitList(user.images?: emptyList())
            imageProfileImagesAdapter.setOnItemClickListener { view, s ->
                sendImageToFullScreenImageActivity(view,s)
            }
        }
    }

    private fun setImageFullScreen() {
        intent.getStringExtra("image").let {
            try {
                val ip: FileInputStream = openFileInput(it)
                val bmp = BitmapFactory.decodeStream(ip)
                ip.close()
                binding.imageFull.setImageBitmap(bmp)
            } catch (e: Exception) {
                GlideApp.loadImage(
                    intent.getStringExtra("url").toString(),
                    binding.imageFull,
                    this
                )
            }
        }
    }

    override fun onBackPressed() {

    }
}