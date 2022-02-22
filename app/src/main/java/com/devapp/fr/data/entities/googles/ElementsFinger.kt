package com.devapp.fr.data.entities.googles

import android.graphics.Color


//Object for canvas View
data class Point(var x:Float,var y:Float)
data class FingerPath(var points:List<Point>,var time:List<Int>){
    fun getXPoints():List<Int>{
        val result = mutableListOf<Int>()
        points.forEach {
            result.add(it.x.toInt())
        }
        return result
    }
    fun getYPoints():List<Int>{
        val result = mutableListOf<Int>()
        points.forEach {
            result.add(it.y.toInt())
        }
        return result
    }
    fun clear(){
        points.toMutableList().clear()
        time.toMutableList().clear()
    }
}

//Object for recycle view in viewpager 2
data class ItemsWordSound(
    var colorCanvas:Int = Color.parseColor("#000000"),
    var colorBrush:Int = Color.parseColor("#ffffff"),
    var sizeBursh:Float = 5f,
    var isEnable:Boolean = false,
    var listWordSuggest:List<String> = emptyList(),
    var textUserChoose:MutableList<String> = mutableListOf(),
    var textCorrect:String,
    var isCorrect:Boolean?=null,
    var sound:String = "",
    var phoneticTextFront:String="",
    var phoneticTextCorrect:String="",
    var meanFront:String="",
    var meanBack:String="",
    var isLoadingWord:Boolean = false,
    var enabledFlipView:Boolean = false
)