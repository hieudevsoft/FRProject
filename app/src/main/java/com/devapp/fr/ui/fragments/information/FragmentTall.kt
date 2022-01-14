package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentTallBinding

class FragmentTall : BaseFragment<FragmentTallBinding>() {
    val TAG = "FragmentTall"
    override fun onSetupView() {
        binding.apply { 
            radio.setOnCheckedChangeListener { _, b ->
                //TO DO CHECKED
            }
            seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    Log.d(TAG, "onProgressChanged: $p1")
                    tvTall.text = "${
                        if(p1==91) "< 91"
                        else if(p1==220) "> 220"
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
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}