package com.devapp.fr.ui.fragments.information

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentTallBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenCreated
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTall : BaseFragment<FragmentTallBinding>() {
    val TAG = "FragmentTall"
    private val args: FragmentTallArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onSetupView() {
        binding.apply {
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    tvTall.text = "${
                        if (p1 == 91) "<91"
                        else if (p1 == 220) ">220"
                        else p1.toString()
                    } cm"
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    radio.isChecked = false
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    Log.d(TAG, "onStopTrackingTouch: Stop checking tall")
                }

            })

            launchRepeatOnLifeCycleWhenStarted {
                sharedViewModel.getSharedFlowTall()
                    .distinctUntilChanged()
                    .collectLatest {
                        if (it != -1) {
                            val data = "${
                                if (it == 91) "<91"
                                else if (it == 220) ">220"
                                else it.toString()
                            } cm"
                            binding.tvTall.text = data
                            binding.seekBar.setProgress(it, true)
                            binding.radio.isChecked = false
                        } else binding.radio.isChecked = true

                    }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        binding.ibBack.setOnClickWithAnimationListener {
            if (binding.radio.isChecked) {
                sharedViewModel.setSharedFlowTall(-1)
            } else {
                sharedViewModel.setSharedFlowTall(binding.seekBar.progress)
            }
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}