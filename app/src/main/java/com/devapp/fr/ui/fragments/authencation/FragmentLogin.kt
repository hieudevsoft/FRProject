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
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.widgets.CustomDialog
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
    private lateinit var dialogLoading: CustomDialog
    private val authViewModel: AuthAndProfileViewModel by viewModels()
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    override fun onAttach(context: Context) {
        dialogLoading = CustomDialog(R.layout.dialog_loading)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscriberObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        lifecycleScope.launchWhenResumed {
            authViewModel.stateLogin.collect {
                when (it) {

                    is ResourceRemote.Loading -> {
                        Log.d(TAG, "subscriberObserver: loading...")
                        dialogLoading.show(childFragmentManager, dialogLoading.tag)
                    }

                    is ResourceRemote.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Đăng nhập thành công ~",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogLoading.dismiss()
                        prefs.saveIsLogin(true)
                        prefs.saveIdUserLogin(it.data)
                        authViewModel.getUserProfile(it.data)
                        Log.d(TAG, "subscriberObserver: ${it.data}")
                        delay(1000)
                        findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentMainViewPager(it.data))
                    }

                    is ResourceRemote.Error -> {
                        dialogLoading.dismiss()
                        Toast.makeText(requireContext(), "Có lỗi xảy ra ~", Toast.LENGTH_SHORT).show()
                    }

                    is ResourceRemote.Empty -> {
                        dialogLoading.dismiss()
                        binding.apply {
                            root.showSnackbar("Sai thông tin đăng nhập!")
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