package com.devapp.fr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.devapp.fr.adapters.IntroduceViewPagerAdapter
import com.devapp.fr.databinding.ActivityInformationUserBinding
import com.devapp.fr.ui.fragments.information.FragmentChooseGender
import com.devapp.fr.ui.fragments.information.FragmentIntroduce

class InformationUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInformationUserBinding
    private lateinit var introduceViewPagerAdapter: IntroduceViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
    }

    private fun setupAdapter(){
        introduceViewPagerAdapter = IntroduceViewPagerAdapter(getListFragment(),supportFragmentManager,lifecycle)
        binding.introduceViewpager2.apply {
            adapter = introduceViewPagerAdapter
            offscreenPageLimit = getListFragment().size
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.progressBarStep.setProgress((position+1)*10,true)
                }
            })
        }
    }

    override fun onBackPressed() {

    }

    private fun getListFragment():List<Fragment>{
        return listOf(
            FragmentIntroduce(),
            FragmentChooseGender()
        )
    }
}