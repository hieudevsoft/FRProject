package com.devapp.fr.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Member(
    val middleName: String,
    val name: String,
    val major: String,
    val majorDes: String,
    val city: String,
    val country: String = "Viet Nam",
    val phoneNumberOne: String,
    val phoneNumberOther: String,
    val email: String,
    val website: String,
    @RawRes val lottieAnimationBack: Int,
    @DrawableRes val imageFront: Int,
)
