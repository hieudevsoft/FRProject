package com.devapp.fr.ui.activities

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.devapp.fr.R
import com.devapp.fr.adapters.InterestAdapter
import com.devapp.fr.adapters.ViewImagePartnerAdapter
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.data.models.items.InterestItem
import com.devapp.fr.databinding.ActivityViewPartnerBinding
import com.devapp.fr.ui.fragments.configProfile.FragmentEditProfile
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.UiHelper.sendImageToFullScreenImageActivity
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import java.io.FileInputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@AndroidEntryPoint
class ViewPartnerActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityViewPartnerBinding
    private lateinit var user: UserProfile
    private lateinit var interestAdapter: InterestAdapter
    private lateinit var imageProfileImagesAdapter: ViewImagePartnerAdapter
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewPartnerBinding.inflate(layoutInflater)
        setInsetsWindow()
        setContentView(binding.root)
        user = intent.getSerializableExtra("data") as UserProfile
        setImageFullScreen()
        setRecyclerViewImage()
        animateBottomCard()
        setupAdapter()
        binding.apply {
            val age =
                Calendar.getInstance().get(Calendar.YEAR) - user.dob.split("/").last().toInt() + 1
            tvUsernameAge.text = "${user.name}, $age"
            val gender = if (user.gender == 0) "Nữ" else "Nam"
            tvGender.text = " - ${gender}"
            tvAddress.text = " ${user.address}"
            lyIcBack.setOnClickWithAnimationListener {
                finish()
            }
            setupSimilarInterest()
            fltButton.setOnClickListener {
                supportFragmentManager.commit {
                    add(R.id.fr_fragment,FragmentEditProfile(user),null)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun setupSimilarInterest() {
        var listInterestMe = prefs.readInterest()
        if ((user.interests == null || user.interests!!.isEmpty()) && listInterestMe!!.isEmpty()) {
            binding.apply {
                circularProgress.setProgress(10.0, 10.0)
                tvNumberSimilar.text = "Hai bạn thật hợp nhau"
            }
        } else
            if (user.interests == null || user.interests!!.isEmpty() || listInterestMe!!.isEmpty()) {
                binding.apply {
                    circularProgress.setProgress(0.0, 10.0)
                    tvNumberSimilar.text = "0 sở thích tương đồng"
                }
            } else {
                var numberSimilar = 0
                val listInterestMeConvert = listInterestMe.split("&")
                val listCompare = listInterestMeConvert.map { it.toInt() }
                listCompare.forEach {
                    if (user.interests!!.contains(it)) numberSimilar += 1
                }
                binding.apply {
                    circularProgress.setProgress(
                        (numberSimilar / min(
                            listCompare.size,
                            user.interests!!.size
                        ).toFloat()* 10).toDouble(), 10.0
                    )
                    tvNumberSimilar.text = "$numberSimilar sở thích tương đồng"
                }
            }
    }

    private fun setInsetsWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            WindowInsetsCompat.CONSUMED
        }
    }


    private fun setupAdapter() {
        val flexLayoutManager = FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP)
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.FLEX_START
        interestAdapter = InterestAdapter()
        binding.rcvInterest.apply {
            layoutManager = flexLayoutManager
            itemAnimator = SlideInLeftAnimator(OvershootInterpolator(2f))
            adapter = interestAdapter
            interestAdapter.submitList(getListInterest(user.interests ?: emptyList()))
        }
    }

    private fun animateBottomCard() {
        binding.apply {
            cardBottom.alpha = 0f
            cardBottom.translationY = 2000f
            cardBottom.animate().translationY(0f).setDuration(2000L).start()
            cardBottom.animate().alpha(1f).setDuration(500L).setStartDelay(1000L).start()
        }
    }

    private fun getListInterest(listInteresYou: List<Int>): List<InterestItem> {
        val listColorSelected = resources.getStringArray(R.array.tag_color)
        val listColorDefault = resources.getStringArray(R.array.tag_color_hint)
        val listInterest = resources.getStringArray(R.array.hobby)
        val listInterestMe = prefs.readInterest()?.split("&")
        val listShow = mutableListOf<InterestItem>()
        listInteresYou.forEach {
            listShow.add(
                InterestItem(
                    "#" + listInterest[it],
                    false,
                    Color.parseColor(listColorDefault[1]),
                    Color.parseColor(listColorSelected[1])
                )
            )
        }
        if(listInterestMe!![0].isNotEmpty()){
            listInterestMe.forEach {
                if(user.interests!!.contains(it.toInt())) listShow[it.toInt()].isSelected = true
            }
        }
        return listShow.toList()
    }


    private fun setRecyclerViewImage() {
        binding.rcImage.apply {
            imageProfileImagesAdapter = ViewImagePartnerAdapter(this@ViewPartnerActivity)
            itemAnimator = SlideInLeftAnimator(OvershootInterpolator(1f))
            layoutManager = LinearLayoutManager(this@ViewPartnerActivity, HORIZONTAL, false)
            adapter = imageProfileImagesAdapter
            imageProfileImagesAdapter.submitList(user.images ?: emptyList())
            imageProfileImagesAdapter.setOnItemClickListener { view, s ->
                sendImageToFullScreenImageActivity(view, s)
            }
        }
    }

    private fun setImageFullScreen() {
        intent.getStringExtra("image").let {
            try {
                val ip: FileInputStream = openFileInput(it)
                val bmp = BitmapFactory.decodeStream(ip)
                ip.close()
                binding.imageFull.setImageBitmap(bmp)
            } catch (e: Exception) {
                GlideApp.loadImage(
                    intent.getStringExtra("url").toString(),
                    binding.imageFull,
                    this
                )
            }
        }
    }

    override fun onBackPressed() {

    }
}