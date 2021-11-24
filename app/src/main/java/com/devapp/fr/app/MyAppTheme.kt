package com.devapp.fr.app

import android.content.Context
import androidx.core.content.ContextCompat
import com.devapp.fr.R
import com.dolatkia.animatedThemeManager.AppTheme

interface MyAppTheme:AppTheme {
    fun textColor(context: Context):Int
    fun backgroundColor(context: Context):Int
}
class DarkTheme():MyAppTheme{
    override fun textColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.background_dark_mode)
    }

    override fun id(): Int {
        return 0
    }

}

class LightTheme():MyAppTheme{
    override fun textColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.background_light_mode)
    }

    override fun id(): Int {
        return 1
    }

}