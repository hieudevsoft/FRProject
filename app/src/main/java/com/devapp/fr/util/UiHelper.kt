package com.devapp.fr.util

import android.content.Context
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import com.devapp.fr.R

object UiHelper {
    fun makeIndicatorCircle(
        numberDot:Int
        ,context: Context
        , layout : LinearLayout
        , position:Int
        , isDarkTheme:Boolean
    ) {
        layout.removeAllViews()
        for ( i in 0 until numberDot) {
            val textView = TextView(context)
            textView.text = Html.fromHtml("&#8226;")
            textView.textSize = 40f
            textView.setPadding(6,6,6,6)
            if(position==i){
                if(isDarkTheme)
                textView.setTextColor(context.resources.getColor(R.color.white))
                else textView.setTextColor(context.resources.getColor(R.color.black))
            }
            else {
                textView.setTextColor(context.resources.getColor(R.color.sub_text_dark_mode))
            }
            layout.addView(textView)
        }
    }
}