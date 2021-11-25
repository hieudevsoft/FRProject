package com.devapp.fr.util

import android.content.Context
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import android.app.Activity
import android.content.DialogInterface
import android.view.View
import androidx.core.content.ContextCompat
import com.devapp.fr.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object UiHelper {
    fun makeIndicatorCircle(
        numberDot:Int
        ,context: Context
        , layout : LinearLayout
        , position:Int
    ) {
        layout.removeAllViews()
        for ( i in 0 until numberDot) {
            val textView = TextView(context)
            textView.text = Html.fromHtml("&#8226;")
            textView.textSize = 40f
            textView.setPadding(6,6,6,6)
            if(position==i){
                textView.setTextColor(context.resources.getColor(R.color.white))
            }
            else {
                textView.setTextColor(context.resources.getColor(R.color.sub_text_dark_mode))
            }
            layout.addView(textView)
        }
    }

    fun triggerBottomAlertDialog(
        activity:Activity,
        title:String,
        message:String,
        textConfirm:String,
        textCancel:String,
        isDarkMode: Boolean,
        callBackYes:(DialogInterface,Int)->Unit
    ){
        val style = if(isDarkMode) R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_DarkTheme else
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_LightTheme
        MaterialAlertDialogBuilder(activity, style)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(textConfirm, callBackYes)
            .setNegativeButton(textCancel){dialogInterface,_->
                dialogInterface.dismiss()
            }
            .show()
    }

    fun View.GONE(){
        this.visibility = View.GONE
    }
    fun View.VISIBLE(){
        this.visibility = View.VISIBLE
    }
    fun View.INVISIBLE(){
        this.visibility = View.INVISIBLE
    }
}