package com.devapp.fr.ui.fragments.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.adapters.AboutUsAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.Member
import com.devapp.fr.databinding.FragmentAbousUsBinding
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class FragmentAbousUs:BaseFragment<FragmentAbousUsBinding>(){
    private lateinit var aboutUsAdapter:AboutUsAdapter
    override fun onSetupView() {
        aboutUsAdapter = AboutUsAdapter()
        binding.recyclerView.adapter = aboutUsAdapter
        binding.recyclerView.itemAnimator = SlideInLeftAnimator(OvershootInterpolator(1f))
        aboutUsAdapter.submitList(
            listOf(
                Member(
                    "tuấn hiệp",
                    "lê ",
                    "designer",
                    "ui/ux Design",
                    "Nghe An Convince",
                    "Viet Nam",
                    "+84 " +
                            "0123457xx",
                    "+84 4456455xx",
                    "hiepdesigner@gmail.com",
                    "www.FrTeam.com.vn",
                    R.raw.json_card_test,
                    R.drawable.img_test
                ),
                Member(
                    "xuân trường",
                    "trần ",
                    "developer",
                    "web developer",
                    "Ha Noi Convince",
                    "Viet Nam",
                    "+84 " +
                            "0123567xx",
                    "+84 4456455xx",
                    "truongwibu@gmail.com",
                    "www.FrTeam.com.vn",
                    R.raw.json_test_1,
                    R.drawable.img_test_1
                ),
                Member(
                    "huy hiệu",
                    "bùi ",
                    "developer",
                    "mobile developer",
                    "Bac Giang Convince",
                    "Viet Nam",
                    "+84" +
                            " " +
                            "0123456xx",
                    "+84 4456455xx",
                    "hieubg1307@gmail.com",
                    "www.FrTeam.com.vn",
                    R.raw.json_test_2,
                    R.drawable.img_test_2
                ),

                )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickWithAnimationListener {
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}