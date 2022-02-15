package com.devapp.fr.util.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.suspendCoroutine

fun Fragment.launchRepeatOnLifeCycleWhenCreated(callBack:suspend (CoroutineScope)->Unit){
    this.lifecycleScope.launchWhenCreated {
        repeatOnLifecycle(Lifecycle.State.CREATED){
            callBack(this)
        }
    }
}

fun Fragment.launchRepeatOnLifeCycleWhenStarted(callBack:suspend (CoroutineScope)->Unit){
    this.lifecycleScope.launchWhenStarted {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            callBack(this)
        }
    }
}


fun Fragment.launchRepeatOnLifeCycleWhenResumed(callBack:suspend (CoroutineScope)->Unit){
    this.lifecycleScope.launchWhenResumed {
        repeatOnLifecycle(Lifecycle.State.RESUMED){
            callBack(this)
        }
    }
}


