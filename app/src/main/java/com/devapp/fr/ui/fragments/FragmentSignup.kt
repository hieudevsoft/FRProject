package com.devapp.fr.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentLoginBinding
import com.devapp.fr.databinding.FragmentSignupBinding
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.Validation

class FragmentSignup : Fragment(R.layout.fragment_signup) {
    val TAG = "FragmentLogin"
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var thread:Thread
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Init View
        initView()

        //Handle event
        handleElementsEvent()

        //Make background thread
        thread = Thread(BackgroundTaskStatusSignUp(
            binding.edtEmail,
            binding.edtName,
            binding.edtPassword,
            binding.edtConfirmPassword,
            binding.btnSignUp
        ))
        thread.start()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        binding.apply {
            edtName.isEnabled = true
            edtPassword.isEnabled = true
            edtConfirmPassword.isEnabled = true
        }
    }

    private fun handleElementsEvent() {
        binding.apply {

            tvLogin.setOnClickListener {
                findNavController().navigate(FragmentSignupDirections.actionFragmentSignupToFragmentLogin())
            }

            btnSignUp.setOnClickListener {
                if(thread.isAlive||!thread.isInterrupted)
                thread.interrupt()
            }


            edtEmail.setOnFocusChangeListener { view, focus ->
                when {
                    focus -> view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                    Validation.validateEmailField(edtEmail.text.toString()) -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
                    }
                    else -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                        edtEmail.error = "Email không hợp lệ!"
                    }
                }
            }

            edtName.setOnFocusChangeListener { view, focus ->
                when {
                    focus -> view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                    Validation.validateNameField(edtName.text.toString()) -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
                    }
                    else -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                        edtName.error = "Tên không hợp lệ!"
                    }
                }
            }

            edtPassword.setOnFocusChangeListener { view, focus ->
                when {
                    focus -> view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                    Validation.validateNameField(edtPassword.text.toString()) -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
                    }
                    else -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                        edtPassword.error = "Mật khẩu phải ít nhất 8 ký tự!"
                    }
                }
            }

            edtConfirmPassword.setOnFocusChangeListener { view, focus ->
                when {
                    focus -> view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                    Validation.validateConfirmPasswordFiled(edtPassword.text.toString(),edtConfirmPassword.text.toString()) -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login)
                    }
                    else -> {
                        view.setBackgroundResource(R.drawable.custom_bg_edittext_login_error)
                        edtPassword.error = "Xác nhận mật khẩu không đúng!"
                    }
                }
            }


            edtEmail.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                true
            }

            edtName.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                true
            }

            edtPassword.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                true
            }

            edtConfirmPassword.setOnKeyListener { view, _, _ ->
                view.setBackgroundResource(R.drawable.custom_bg_edittext_login_focus)
                true
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

    class BackgroundTaskStatusSignUp(
        private val email:EditText,
        private val name:EditText,
        private val password:EditText,
        private val confirmPassword:EditText,
        private val button:Button
    ):Runnable{
        override fun run() {
            val handler = Handler(Looper.getMainLooper())
            while (true){
                if(!Validation.validateSignUpFields(
                            email.text.toString(),
                            name.text.toString(),
                            password.text.toString(),
                            confirmPassword.text.toString()
                    )){
                        handler.post {
                            button.isEnabled = false
                            button.setBackgroundResource(R.drawable.custom_bg_button_login_disable)
                        }
                    }else{
                        handler.post {
                            button.isEnabled = true
                            button.setBackgroundResource(R.drawable.custom_bg_button_login)
                        }
                    }
            }
        }

    }
}