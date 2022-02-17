package com.devapp.fr.data.models.items

import androidx.annotation.DrawableRes

data class InformationItem(
    @DrawableRes var icon:Int,
    var title:String,
    var content:String
    )