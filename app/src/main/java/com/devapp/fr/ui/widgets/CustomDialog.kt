package com.devapp.fr.ui.widgets

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.lang.Exception

class CustomDialog(private val res:Int): DialogFragment() {

    private lateinit var viewInflate: View
    override fun onCreate(savedInstanceState: Bundle?) {
        viewInflate = LayoutInflater.from(this.context).inflate(res,null)
        super.onCreate(savedInstanceState)
    }

}
