package com.devapp.fr.util

import android.content.Context
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.appcompat.widget.AppCompatEditText
import com.devapp.fr.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

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


    fun AppCompatEditText.multilineIme(action: Int) {
        imeOptions = action
        inputType = EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
        setHorizontallyScrolling(false)
        maxLines = Integer.MAX_VALUE
    }
    fun AppCompatEditText.multilineIme(action:Int,callback: (() -> Unit)? = null) {
        multilineIme(action)
        setOnEditorActionListener { _, actionId, _ ->
            if (action == actionId) {
                callback?.invoke()
                true
            }
            false
        }
    }

    fun View.hideKeyboard() {
        val inputMethodManager = this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.showKeyboard() {
        val inputMethodManager = this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(SHOW_FORCED, HIDE_IMPLICIT_ONLY)
    }

    fun View.showSnackbar(message:String, callBack: (() -> Unit?)? =null){
        Snackbar.make(
            this,
            message,
            Snackbar.LENGTH_LONG
        ).setAction("Đã hiểu!"){
            if (callBack != null) {
                callBack()
            }
        }.show()
    }

}