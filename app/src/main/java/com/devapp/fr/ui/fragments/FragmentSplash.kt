package com.devapp.fr.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.devapp.fr.R
import com.devapp.fr.adapters.SlidePagerAdapter
import com.devapp.fr.data.models.items.SlideItem
import com.devapp.fr.databinding.FragmentSplashBinding
import com.devapp.fr.ui.MainActivity
import com.devapp.fr.util.AnimationHelper.startAnimClick
import com.devapp.fr.util.PageTransformHelper
import com.devapp.fr.util.UiHelper

class FragmentSplash : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var slideViewPager: SlidePagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Initialize slide pager
        initializeViewPager()

        //Setup theme
        setupTheme()

        //Handle event elements
        handleEventElements()

        //Handle event view pager
        handleEventViewPager()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleEventViewPager() {
        binding.splashViewPager2.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                UiHelper.makeIndicatorCircle(3,requireContext(),binding.layoutDot,position)
            }
        })
    }

    private fun handleEventElements() {
        binding.btnSkip.setOnClickListener {
            it.startAnimClick()
            (requireActivity() as MainActivity).navigateToDestination(R.id.fragmentMainViewPager)
        }
    }

    private fun setupTheme() {
        UiHelper.makeIndicatorCircle(3,requireContext(),binding.layoutDot,0)
            ViewCompat.setBackgroundTintList(
                binding.btnSkip,
                ColorStateList.valueOf(
                    resources.getColor(
                        R.color.background_dialog_light,
                    )
                )
            )
            binding.btnSkip.setTextColor(resources.getColor(R.color.background_dark_mode))
            binding.root.setBackgroundColor(
                resources.getColor(
                    R.color.background_dark_mode,
                )
            )
        binding.btnSkip.iconTint = ColorStateList.valueOf(resources.getColor(R.color.background_dark_mode))
    }

    private fun initializeViewPager() {
        val colorText: Int = Color.WHITE
        slideViewPager = SlidePagerAdapter(makeListViewPager(), colorText)
        binding.splashViewPager2.apply {
            adapter = slideViewPager
            setPageTransformer(PageTransformHelper.CubeOutPageTransformer())
        }
    }

    private fun makeListViewPager(): List<SlideItem> {
        return listOf(
            SlideItem(resources.getString(R.string.title1), resources.getString(R.string.des1), R.drawable.img_splashscreen1),
            SlideItem(resources.getString(R.string.title2), resources.getString(R.string.des2), R.drawable.img_splashscreen2),
            SlideItem(resources.getString(R.string.title3), resources.getString(R.string.des3), R.drawable.img_splashscreen3)
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}