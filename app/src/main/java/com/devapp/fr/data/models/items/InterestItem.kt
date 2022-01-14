package com.devapp.fr.data.models.items

data class InterestItem(
    val itemName : String,
    var isSelected : Boolean,
    val itemBgColorDefault : Int,
    val itemBgColorSelected : Int?=null,
    val icon : Int?=null
)