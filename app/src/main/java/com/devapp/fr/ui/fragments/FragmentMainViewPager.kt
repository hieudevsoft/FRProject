package com.devapp.fr.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.devapp.fr.R
import com.devapp.fr.adapters.MainViewPagerAdapter
import com.devapp.fr.databinding.FragmentChatsBinding
import com.devapp.fr.databinding.FragmentMainViewPagerBinding
import com.devapp.fr.databinding.FragmentSettingsBinding
import com.devapp.fr.ui.MainActivity
import com.devapp.fr.util.PageTransformHelper
import nl.joery.animatedbottombar.AnimatedBottomBar

class FragmentMainViewPager : Fragment(R.layout.fragment_main_view_pager) {

    private var _binding: FragmentMainViewPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomBar:AnimatedBottomBar
    private lateinit var mainViewPager:MainViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Get Bottom Bar from MainActivity
        if(requireActivity() is MainActivity){
            bottomBar = (requireActivity() as MainActivity).bottomBar
        }

        //Initialize MainViewPager
        initializeMainViewPager()

        //Setup ViewPager2 with bottom bar
        bottomBar.setupWithViewPager2(binding.mainViewPager)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeMainViewPager() {
        mainViewPager = MainViewPagerAdapter(makeListFragment(),childFragmentManager,lifecycle)
        binding.mainViewPager.apply {
            adapter = mainViewPager
            setPageTransformer(PageTransformHelper.TabletPageTransformer())
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun makeListFragment ():List<Fragment>{
        return listOf(
            FragmentSettings(),
            FragmentChats()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}