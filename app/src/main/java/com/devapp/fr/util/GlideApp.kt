package com.devapp.fr.util

import androidx.fragment.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devapp.fr.R

object GlideApp {
    fun loadImage(uri:String,imageView:ImageView,fragment:Fragment){
        val context = imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(fragment).load(uri).apply(option).into(imageView)
    }
}