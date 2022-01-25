package com.devapp.fr.util

import android.app.Activity
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devapp.fr.R
import de.hdodenhof.circleimageview.CircleImageView

object GlideApp {
    fun loadImage(uri:String,imageView:ImageView,fragment:Fragment){
        imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(fragment).load(uri).apply(option).into(imageView)
    }

    fun loadImage(uri:String,imageView:AppCompatImageView,fragment:Fragment){
        imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(fragment).load(uri).apply(option).into(imageView)
    }


    fun loadImage(uri:String,imageView:CircleImageView,fragment:Fragment){
        imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(fragment).load(uri).apply(option).into(imageView)
    }

    fun loadImage(uri:String,imageView:ImageView,activity:Activity){
        imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(activity).load(uri).apply(option).into(imageView)
    }

    fun loadImage(uri:String,imageView:AppCompatImageView,activity:Activity){
        imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(activity).load(uri).apply(option).into(imageView)
    }


    fun loadImage(uri:String,imageView:CircleImageView,activity:Activity){
        imageView.context
        val option = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_react_wow)
            .skipMemoryCache(false)

        Glide.with(activity).load(uri).apply(option).into(imageView)
    }
}