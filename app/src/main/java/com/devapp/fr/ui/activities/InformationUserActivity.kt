package com.devapp.fr.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.devapp.fr.R
import com.devapp.fr.adapters.IntroduceViewPagerAdapter
import com.devapp.fr.databinding.ActivityInformationUserBinding
import com.devapp.fr.ui.fragments.information.*
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toInvisible
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener

class InformationUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInformationUserBinding
    private lateinit var introduceViewPagerAdapter: IntroduceViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        handleEvent()
    }

    private fun handleEvent() {
        binding.apply {
            cardBtnPrev.setOnClickWithAnimationListener {
                val currentPosition = introduceViewpager2.currentItem
                introduceViewpager2.currentItem = currentPosition-1
            }

            cardBtnContinue.setOnClickWithAnimationListener {
                val currentPosition = introduceViewpager2.currentItem
                introduceViewpager2.currentItem = currentPosition+1
            }
        }
    }

    private fun setupAdapter(){
        introduceViewPagerAdapter = IntroduceViewPagerAdapter(getListFragment(),supportFragmentManager,lifecycle)
        binding.introduceViewpager2.apply {
            adapter = introduceViewPagerAdapter
            offscreenPageLimit = getListFragment().size
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.progressBarStep.setProgress((position+1)*10,true)
                    if(position==0) binding.cardBtnPrev.toInvisible() else binding.cardBtnPrev.toVisible()
                    if(position==introduceViewPagerAdapter.itemCount-1) binding.cardBtnContinue.toInvisible() else binding.cardBtnContinue.toVisible()
                }
            })
        }
    }

    override fun onBackPressed() {

    }

    private fun getListFragment():List<Fragment>{
        return listOf(
            FragmentIntroduce(),
            FragmentChooseGender(),
            FragmentMaritalStatus(),
            FragmentTall(),
            FragmentSchool(),
            FragmentUserJob(),
            FragmentDrink()
        )
    }
}