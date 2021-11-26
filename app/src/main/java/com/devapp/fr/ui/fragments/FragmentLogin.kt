package com.devapp.fr.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentLoginBinding
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.util.UiHelper.showSnackbar

class FragmentLogin : Fragment(R.layout.fragment_login) {
    val TAG = "FragmentLogin"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Handle event
        handleElementsEvent()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleElementsEvent() {
        binding.apply {

            edtEmail.setOnFocusChangeListener { view, focus ->
                if(focus) view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                else view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
            }

            edtPassword.setOnFocusChangeListener { view, focus ->
                if(focus) view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                else view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
            }

            edtEmail.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                true
            }

            edtPassword.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                true
            }


            btnLogin.setOnClickListener {
                root.showSnackbar("Sai thông tin đăng nhập!")
                edtEmail.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                edtPassword.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
            }

            tvSignUp.setOnClickListener {
                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentSignup2())
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }
}