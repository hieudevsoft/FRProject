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
import androidx.fragment.app.FragmentManager

class CustomDialog(private val res:Int): DialogFragment() {

    private lateinit var viewInflate: View

    override fun onCreate(savedInstanceState: Bundle?) {
        viewInflate = LayoutInflater.from(this.context).inflate(res,null)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return viewInflate
    }
    private var viewCallBack: ((View) -> Unit)? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogResult = AlertDialog.Builder(requireActivity()).setView(viewInflate).create()
        dialogResult.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewCallBack?.let { it(viewInflate) }
        return dialogResult
    }

    fun setOnViewCallBackListener(callback: ((View) -> Unit)){
        viewCallBack = callback
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try{
            if(!isAdded) super.show(manager,tag)
        }catch (e:Exception){

        }
    }
}
