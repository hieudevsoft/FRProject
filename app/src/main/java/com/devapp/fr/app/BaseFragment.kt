package com.devapp.fr.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.devapp.fr.R
import com.devapp.fr.ui.widgets.CustomDialog
import java.lang.Exception

abstract class BaseFragment<V : ViewBinding> : Fragment() {

    private var _binding: V? = null
    protected val binding get() = _binding!!
    protected lateinit var loadingDialog:CustomDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = getBinding(inflater, container)
        onSetupView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun onSetupView()
    protected fun showLoadingDialog(){
        try {
            val fm =  childFragmentManager
            val oldFragment: Fragment? = fm.findFragmentByTag("loading_dialog")
            if (oldFragment != null && oldFragment.isAdded) return
            if (oldFragment == null && !loadingDialog.isAdded && !loadingDialog.isVisible) {
                fm.executePendingTransactions()
                loadingDialog.show(fm, "loading_dialog")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    protected fun hideLoadingDialog(){
        try {
            val fm =  childFragmentManager
            val oldFragment: Fragment? = fm.findFragmentByTag("loading_dialog")
            if (oldFragment != null && loadingDialog.isAdded && loadingDialog.isVisible) {
                fm.executePendingTransactions()
                hideLoadingDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
