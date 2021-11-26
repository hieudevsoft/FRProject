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
import com.devapp.fr.databinding.FragmentSignupBinding
import com.devapp.fr.util.UiHelper.showSnackbar

class FragmentSignup : Fragment(R.layout.fragment_signup) {
    val TAG = "FragmentLogin"
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Handle event
        handleElementsEvent()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleElementsEvent() {
        binding.apply {

            tvLogin.setOnClickListener {
                findNavController().navigate(FragmentSignupDirections.actionFragmentSignupToFragmentLogin())
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

    class BackgroundTaskStatusSignUp:Runnable{
        override fun run() {
            TODO("Not yet implemented")
        }

    }
}