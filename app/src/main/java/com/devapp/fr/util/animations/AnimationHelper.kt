package com.devapp.fr.util.animations

import android.view.View
import android.view.animation.AnimationUtils
import com.devapp.fr.R

object AnimationHelper {
    fun View.startAnimClick(){
        this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.animation_click))
    }
}