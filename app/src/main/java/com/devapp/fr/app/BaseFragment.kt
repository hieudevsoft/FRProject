package com.devapp.fr.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.devapp.fr.R
import com.devapp.fr.ui.widgets.LoadingDialog
import java.lang.Exception

abstract class BaseFragment<V : ViewBinding> : Fragment() {
    val LOG = "BaseFragment"
    private var _binding: V? = null
    protected val binding get() = _binding!!
    protected lateinit var loadingDialog:LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadingDialog = LoadingDialog(requireActivity())
        _binding = getBinding(inflater, container)
        onSetupView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun onSetupView()
}
