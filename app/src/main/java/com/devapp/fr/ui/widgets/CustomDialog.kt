package com.devapp.fr.ui.widgets

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.devapp.fr.R


class LoadingDialog(private val activity:Activity) {
    private lateinit var dialog:AlertDialog
    fun show(){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_loading,null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = 600
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        val win: Window? = dialog.window
        win?.attributes = layoutParams
        win?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity,android.R.color.transparent)))

    }
    fun dismiss() = dialog.dismiss()
}
