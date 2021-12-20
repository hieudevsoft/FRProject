package com.devapp.fr.util

import android.content.Context
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.appcompat.widget.AppCompatEditText
import com.devapp.fr.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import android.util.DisplayMetrics
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.github.pgreze.reactions.PopupGravity
import com.github.pgreze.reactions.ReactionsConfig
import com.github.pgreze.reactions.ReactionsConfigBuilder
import com.github.pgreze.reactions.dsl.reactionConfig
import com.github.pgreze.reactions.dsl.reactions
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlin.math.roundToInt
import android.view.animation.DecelerateInterpolator

import android.animation.ObjectAnimator





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


    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun scaleDown(
        realImage: Bitmap, maxImageSize: Float,
        filter: Boolean
    ): Bitmap? {
        val ratio = (maxImageSize / realImage.width).coerceAtMost(maxImageSize / realImage.height)
        val width = (ratio * realImage.width).roundToInt()
        val height = (ratio * realImage.height).roundToInt()
        return Bitmap.createScaledBitmap(
                realImage, width,
                height, filter
            )
    }


    const val RADIUS_POP_UP=16
    fun configReactionPopUp(context:Context,isMe: Boolean): ReactionsConfig {
        return reactionConfig(context){
            reactions {
                reaction { R.drawable.ic_react_like scale ImageView.ScaleType.CENTER_INSIDE }
                reaction { R.drawable.ic_react_love scale ImageView.ScaleType.CENTER_INSIDE }
                reaction { R.drawable.ic_react_laugh scale ImageView.ScaleType.CENTER_INSIDE }
                reaction { R.drawable.ic_react_wow scale ImageView.ScaleType.CENTER_INSIDE }
                reaction { R.drawable.ic_react_sad scale ImageView.ScaleType.CENTER_INSIDE }
                reaction { R.drawable.ic_react_angry scale ImageView.ScaleType.CENTER_INSIDE }
            }
            popupGravity = if(isMe) PopupGravity.PARENT_RIGHT else PopupGravity.PARENT_LEFT
            popupCornerRadius =
                TypedValue.applyDimension(COMPLEX_UNIT_DIP, RADIUS_POP_UP.toFloat(), context.resources.displayMetrics)
                    .toInt()
            popupColor = Color.WHITE
            popupAlpha = 230

            reactionSize = context.resources.getDimensionPixelSize(R.dimen.SIZE_ICON_24)
            horizontalMargin = context.resources.getDimensionPixelSize(R.dimen.MARGIN_SMALL)


            reactionTextProvider = { position -> "Item $position" }
            reactionTexts = R.array.reactions

            textBackground = ColorDrawable(Color.TRANSPARENT)
            textColor = Color.WHITE
            textHorizontalPadding = context.resources.getDimension(R.dimen.PADDING_SMALL).toInt()
            textVerticalPadding = context.resources.getDimension(R.dimen.PADDING_SMALL).toInt()
        }

    }
    fun getSymbolReactionImageByPosition(position:Int):Int{
        return when(position){
            0-> R.drawable.ic_react_like
            1-> R.drawable.ic_react_love
            2-> R.drawable.ic_react_laugh
            3-> R.drawable.ic_react_wow
            4-> R.drawable.ic_react_sad
            5-> R.drawable.ic_react_angry
            else-> -1
        }
    }
    fun Any.findOnClickListener(vararg views:View,block:View.()->Unit){
        views.forEach {
            it.setOnClickListener { it.block() }
        }
    }


    fun View.enableOrNot(enable:Boolean){
        ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(this.context.resources.getColor(if(enable) R.color.background_dialog_dark else R.color.color_grey_300)))
        this.isEnabled = enable
    }

    fun AppCompatEditText.getStringText() = this.text.toString()
    fun AppCompatEditText.setEmptyText() = this.setText("")
    fun EditText.getStringText() = this.text.toString()
    fun EditText.setEmptyText() = this.setText("")
    fun checkAllEmptyAppCompatEditText(vararg items:AppCompatEditText):Boolean{
        items.forEach {
            if(it.getStringText().isNotEmpty()) return false
        }
        return true
    }

    fun LinearProgressIndicator.setProgressAnimate(progress:Int){
        val animation: ObjectAnimator =
            ObjectAnimator.ofInt(this, "progress", this.progress, progress)
        animation.duration = 1000
        animation.setAutoCancel(true)
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    fun View.toGone() = run { this.visibility = View.GONE }
    fun View.toVisible() = run { this.visibility = View.VISIBLE }
}