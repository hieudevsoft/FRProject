package com.devapp.fr.data.models.interfaces

import com.devapp.fr.data.entities.googles.FingerPath

interface HandFingerCallback {
    fun onDrawFinger(listFingerPath:List<FingerPath>)
}