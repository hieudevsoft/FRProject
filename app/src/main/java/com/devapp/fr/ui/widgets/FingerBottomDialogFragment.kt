package com.devapp.fr.ui.widgets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.adapters.SuggestWordAdapter
import com.devapp.fr.data.models.interfaces.HandFingerCallback
import com.devapp.fr.databinding.LayoutDialogSexualityBinding
import com.devapp.fr.databinding.LyCardHandWriteBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.HandWriterViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
open class FingerBottomDialogFragment(
    private val fingerCallback: HandFingerCallback,
    private val handWriterViewModel: HandWriterViewModel,
    private val textCallBack:(String)->Unit,
    ):BottomSheetDialogFragment() {
    val TAG = "FingerBottomDialogFragment"
    private lateinit var suggestWordAdapter:SuggestWordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ly_card_hand_write, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = getView()?.let { LyCardHandWriteBinding.bind(it) }
        if (binding != null) {
            suggestWordAdapter = SuggestWordAdapter { textCallBack(it) }
            binding.canvasFinger.setCallback(fingerCallback)
            binding.cvUndo.setOnClickWithAnimationListener { binding.canvasFinger.undo() }
            binding.cvClear.setOnClickWithAnimationListener { binding.canvasFinger.clear() }
            handWriterViewModel.resultResponse.observe(this){
                if (it != null) {
                    try {
                        if (it[1] != null && it[1] is List<Any?>) {
                            val temp = it[1] as List<Any?>
                            if (temp[0] != null && temp[0] is List<Any?>) {
                                val temp = temp[0] as List<Any?>
                                if (temp[1] != null && temp[1] is List<*>) {
                                    val data = temp[1] as List<String>
                                    suggestWordAdapter.setData(data)
                                    binding.recycleView.adapter = suggestWordAdapter
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            setBottomSheetExpanded(dialog)
        }
        return dialog
    }


    open fun setBottomSheetExpanded(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.let {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            val layoutParams = bottomSheet.layoutParams
            bottomSheet.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}