package com.devapp.fr.ui.fragments.configProfile

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentFramentDateOfBirthBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.getStringText
import com.devapp.fr.util.UiHelper.setEmptyText
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class FragmentDateOfBirth : BaseFragment<FragmentFramentDateOfBirthBinding>() {
    private lateinit var sharedPref: SharedPreferencesHelper

    override fun onAttach(context: Context) {
        if (context is ConfigProfileActivity) sharedPref = context.sharedPrefs
        super.onAttach(context)
    }

    private val args: FragmentDateOfBirthArgs by navArgs()
    override fun onSetupView() {
        args.let {
            binding.apply {
                binding.tv1.text = "Rất vui gặp bạn, ${args.name}. Sinh\nnhật của bạn khi nào"
                if (sharedPref.readDobConfig().isNotEmpty()) {
                    val element = sharedPref.readDobConfig().split("/")
                    edtItem1Day.setText(element[0][0].toString())
                    edtItem2Day.setText(element[0][1].toString())
                    edtItem1Month.setText(element[1][0].toString())
                    edtItem2Month.setText(element[1][1].toString())
                    edtItem1Year.setText(element[2][0].toString())
                    edtItem2Year.setText(element[2][1].toString())
                    edtItem3Year.setText(element[2][2].toString())
                    edtItem4Year.setText(element[2][3].toString())
                }
                binding.btnContinue.enableOrNot(false)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleEvent()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleEvent() {
        val jobButton: Job = lifecycleScope.launchWhenStarted {
            while (true) {
                binding.apply {
                    if (edtItem1Day.getStringText().isEmpty()) edtItem1Day.requestFocus()
                    else if (edtItem2Day.getStringText().isEmpty()) edtItem2Day.requestFocus()
                    else if (edtItem1Month.getStringText().isEmpty()) edtItem1Month.requestFocus()
                    else if (edtItem2Month.getStringText().isEmpty()) edtItem2Month.requestFocus()
                    else if (edtItem1Year.getStringText().isEmpty()) edtItem1Year.requestFocus()
                    else if (edtItem2Year.getStringText().isEmpty()) edtItem2Year.requestFocus()
                    else if (edtItem3Year.getStringText().isEmpty()) edtItem3Year.requestFocus()
                    else if (edtItem4Year.getStringText().isEmpty()) edtItem4Year.requestFocus()
                    else btnContinue.enableOrNot(true)
                    edtItem4Year.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem4Year.getStringText().isEmpty()) {
                                edtItem3Year.requestFocus()
                            } else edtItem4Year.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem3Year.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem3Year.getStringText().isEmpty()) {
                                edtItem2Year.requestFocus()
                            } else edtItem3Year.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem3Year.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem3Year.getStringText().isEmpty()) {
                                edtItem2Year.requestFocus()
                            } else edtItem3Year.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem2Year.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem2Year.getStringText().isEmpty()) {
                                edtItem1Year.requestFocus()
                            } else edtItem2Year.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem1Year.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem1Year.getStringText().isEmpty()) {
                                edtItem2Month.requestFocus()
                            } else edtItem1Year.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem2Month.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem2Month.getStringText().isEmpty()) {
                                edtItem1Month.requestFocus()
                            } else edtItem2Month.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem1Month.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem1Month.getStringText().isEmpty()) {
                                edtItem2Day.requestFocus()
                            } else edtItem1Month.setEmptyText()
                        }
                        return@setOnKeyListener false
                    }
                    edtItem2Day.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            if (edtItem2Day.getStringText().isEmpty()) {
                                edtItem1Day.requestFocus()
                            } else edtItem2Day.setEmptyText()
                        }
                        return@setOnKeyListener false

                    }
                    edtItem1Day.setOnKeyListener { _, _, keyEvent ->
                        if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                            edtItem1Day.setEmptyText()
                        }
                        return@setOnKeyListener false

                    }
                }
                delay(1000)
            }
        }
        binding.btnContinue.setOnClickListener {
            jobButton.cancel()
            it.startAnimClick()
            sharedPref.saveDobConfig(
                "${binding.edtItem1Day.getStringText()}${binding.edtItem2Day.getStringText()}/" +
                        "${binding.edtItem1Month.getStringText()}${binding.edtItem2Month.getStringText()}/" +
                        "${binding.edtItem1Year.getStringText()}${binding.edtItem2Year.getStringText()}" +
                        "${binding.edtItem3Year.getStringText()}${binding.edtItem4Year.getStringText()}"
            )
            findNavController().navigate(FragmentDateOfBirthDirections.actionFragmentDateOfBirthToFragmentAddress())
        }
    }
}