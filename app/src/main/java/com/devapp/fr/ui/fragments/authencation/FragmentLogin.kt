package com.devapp.fr.ui.fragments.authencation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentLoginBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.NetworkHelper
import com.devapp.fr.util.UiHelper.hideKeyboard
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLogin : BaseFragment<FragmentLoginBinding>() {
    val TAG = "FragmentLogin"

    private val authViewModel: AuthAndProfileViewModel by viewModels()
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    override fun onAttach(context: Context) {

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscriberObserver()
        if(prefs.readIsLogin()){
            startActivity(Intent(requireActivity(),MainActivity::class.java))
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        lifecycleScope.launchWhenResumed {
            authViewModel.stateLogin.collect {
                when (it) {

                    is ResourceRemote.Loading -> {
                        Log.d(TAG, "subscriberObserver: loading...")
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "????ng nh???p th??nh c??ng ~",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismiss()
                        prefs.saveIsLogin(true)
                        prefs.saveIdUserLogin(it.data)
                        prefs.saveProcessRegister(0)
                        Log.d(TAG, "subscriberObserver: ${it.data}")
                        delay(1000)
                        startActivity(Intent(requireActivity(),MainActivity::class.java))
                    }

                    is ResourceRemote.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), "C?? l???i x???y ra ~", Toast.LENGTH_SHORT).show()
                    }

                    is ResourceRemote.Empty -> {
                        loadingDialog.dismiss()
                        binding.apply {
                            root.showSnackbar("Sai th??ng tin ????ng nh???p!")
                            edtEmail.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                            edtPassword.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                        }
                    }

                    else->{

                    }
                }
            }
        }
    }

    private fun handleElementsEvent() {
        binding.apply {

            edtEmail.setOnFocusChangeListener { view, focus ->
                if (focus) view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                else view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
            }

            edtPassword.setOnFocusChangeListener { view, focus ->
                if (focus) view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                else view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
            }

            edtEmail.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                false
            }

            edtPassword.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                false
            }


            btnLogin.setOnClickWithAnimationListener {
                NetworkHelper.onNetWorkConnectedListener(binding.root){
                    authViewModel.loginWitEmailAndPassword(
                        edtEmail.text.toString().trim(),
                        edtPassword.text.toString().trim()
                    )
                }
                it.hideKeyboard()
            }

            tvSignUp.setOnClickListener {
                Intent(requireActivity(), ConfigProfileActivity::class.java).also {
                    startActivity(it)
                    requireActivity().overridePendingTransition(
                        R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_right
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        authViewModel.resetStateLogin()
        super.onDestroy()
    }


    override fun onSetupView() {
        handleElementsEvent()
    }
}