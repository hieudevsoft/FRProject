package com.devapp.fr.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentLoginBinding
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.ui.viewmodels.AuthViewModel
import com.devapp.fr.util.CustomDialog
import com.devapp.fr.util.UiHelper.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentLogin : Fragment(R.layout.fragment_login) {
    val TAG = "FragmentLogin"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogLoading:CustomDialog
    private val authViewModel:AuthViewModel by viewModels()

    override fun onAttach(context: Context) {
        dialogLoading = CustomDialog(R.layout.dialog_loading)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleElementsEvent()
        subscriberObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        lifecycleScope.launchWhenResumed {
            authViewModel.stateLogin.collect {
                when(it){

                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentSettings())
                        Toast.makeText(requireContext(), "Đăng nhập thành công ~", Toast.LENGTH_SHORT).show()
                    }

                    is ResourceRemote.Error->{
                        binding.apply {
                            root.showSnackbar("Sai thông tin đăng nhập!")
                            edtEmail.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                            edtPassword.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                        }
                    }

                }
            }
        }
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
                authViewModel.loginWitEmailAndPassword(
                    edtEmail.text.toString().trim(),
                    edtPassword.text.toString().trim()
                )
            }

            tvSignUp.setOnClickListener {
                Intent(requireActivity(),ConfigProfileActivity::class.java).also {
                    startActivity(it)
                    requireActivity().overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right)
                }
            }
        }
    }

    override fun onDestroy() {
        authViewModel.resetStateLogin()
        super.onDestroy()
    }

    override fun onDestroyView() {
        authViewModel.resetStateLogin()
        super.onDestroyView()
        _binding = null
    }
}