package com.devapp.fr.util.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick

fun Fragment.composeElementsSameClickEvent(vararg views :View,listener:(View)->Unit){
    views.forEach {
        it.setOnClickListener(listener)
    }
}

fun Fragment.showToast(msg:String){
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.scaleDown(){
    this.scaleX = 0.5F
    this.scaleY = 0.5F
}
fun View.scaleUp(){
    this.animate().scaleX(1F).setDuration(500).start()
    this.animate().scaleY(1F).setDuration(500).start()
}