package com.devapp.fr.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.devapp.fr.R
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.*
import com.devapp.fr.databinding.*
import com.devapp.fr.util.Constants
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE
import com.github.pgreze.reactions.ReactionPopup
import com.github.pgreze.reactions.ReactionSelectedListener


class ChatsMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = "ChatsMessageAdapter"

    private inner class ViewHolderMe(val binding: ItemChatMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MessageText) {
            initViewForMessageTextMe(data,binding)
        }
    }

    private fun initViewForMessageTextMe(data:MessageText,binding:ItemChatMeBinding){
        binding.tvTimeMe.GONE()
        binding.tvTimeMe.text = data.time
        binding.tvMessageMe.text = data.message
        val reactionMe = data.react.first()
        val reactionPartner = data.react.last()
        if (reactionMe.count != 0) {
            binding.apply {
                cardViewReactionOne.VISIBLE()
                imgReactionOne.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionMe.react))
                tvCountReactionOne.text = reactionMe.count.toString()
            }
        } else binding.cardViewReactionOne.GONE()
        if (reactionPartner.count != 0) {
            binding.apply {
                cardViewReactionTwo.VISIBLE()
                imgReactionTwo.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionPartner.react))
                tvCountReactionTwo.text = reactionPartner.count.toString()
            }
        } else binding.cardViewReactionTwo.GONE()
    }

    private inner class ViewHolderPartner(val binding: ItemChatPartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MessageText) {
            initViewForMessageTextPartner(data, binding)
        }
    }

    private fun initViewForMessageTextPartner(data: MessageText, binding: ItemChatPartnerBinding) {
        binding.tvTimePartner.GONE()
        binding.tvTimePartner.text = data.time
        binding.tvMessagePartner.text = data.message
        val reactionMe = data.react.first()
        val reactionPartner = data.react.last()
        if (reactionMe.count != 0) {
            binding.apply {
                cardViewReactionOne.VISIBLE()
                imgReactionOne.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionMe.react))
                tvCountReactionOne.text = reactionMe.count.toString()
            }
        } else binding.cardViewReactionOne.GONE()
        if (reactionPartner.count != 0) {
            binding.apply {
                cardViewReactionTwo.VISIBLE()
                imgReactionTwo.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionPartner.react))
                tvCountReactionTwo.text = reactionPartner.count.toString()
            }
        } else binding.cardViewReactionTwo.GONE()
    }

    private inner class ViewHolderImageMe(val binding: ItemChatImageMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(data: MessageImage) {
            binding.tvTimeMe.GONE()
            binding.tvTimeMe.text = data.time
            val reactionMe = data.react.first()
            val reactionPartner = data.react.last()
            if (reactionMe.count != 0) {
                binding.apply {
                    cardViewReactionOne.VISIBLE()
                    imgReactionOne.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionMe.react))
                    tvCountReactionOne.text = reactionMe.count.toString()
                }
            } else binding.cardViewReactionOne.GONE()
            if (reactionPartner.count != 0) {
                binding.apply {
                    cardViewReactionTwo.VISIBLE()
                    imgReactionTwo.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionPartner.react))
                    tvCountReactionTwo.text = reactionPartner.count.toString()
                }
            } else binding.cardViewReactionTwo.GONE()
            if (binding.root.context != null) {
                Glide
                    .with(binding.root.context)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .load(data.urlImage)
                    .centerInside()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_broken_image)
                    .error(R.drawable.ic_error_image)
                    .into(object : CustomViewTarget<ImageView, Bitmap>(binding.imageMe) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            Log.d(TAG, "onLoadFailed: error load $data.id")
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val maxSize = binding.root.context.resources.getDimension(R.dimen.MAX_SIZE_IMAGE_MASSAGE)
                            val result = UiHelper.scaleDown(resource, maxSize, true)
                            Log.d(
                                TAG,
                                "onResourceReady: ${resource.width} ${resource.height} ${result?.width} ${result?.height}"
                            )
                            binding.imageMe.setImageBitmap(result)
                            binding.imageMe.isDrawingCacheEnabled = true
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {

                        }

                    })
            }

        }
    }

    private inner class ViewHolderImagePartner(val binding: ItemChatImagePartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MessageImage) {
            binding.tvTimePartner.GONE()
            binding.tvTimePartner.text = data.time
            val reactionMe = data.react.first()
            val reactionPartner = data.react.last()
            if (reactionMe.count != 0) {
                binding.apply {
                    cardViewReactionOne.VISIBLE()
                    imgReactionOne.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionMe.react))
                    tvCountReactionOne.text = reactionMe.count.toString()
                }
            } else binding.cardViewReactionOne.GONE()
            if (reactionPartner.count != 0) {
                binding.apply {
                    cardViewReactionTwo.VISIBLE()
                    imgReactionTwo.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionPartner.react))
                    tvCountReactionTwo.text = reactionPartner.count.toString()
                }
            } else binding.cardViewReactionTwo.GONE()
            if (binding.root.context != null) {
                Glide
                    .with(binding.root.context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .load(data.urlImage)
                    .centerInside()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.ic_broken_image)
                    .error(R.drawable.ic_error_image)
                    .into(object : CustomViewTarget<ImageView, Bitmap>(binding.imagePartner) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            Log.d(TAG, "onLoadFailed: error load $data.id")
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val maxSize =
                                binding.root.context.resources.getDimension(R.dimen.MAX_SIZE_IMAGE_MASSAGE)
                            val result = UiHelper.scaleDown(resource, maxSize, true)
                            Log.d(
                                TAG,
                                "onResourceReady: ${resource.width} ${resource.height} ${result?.width} ${result?.height}"
                            )
                            binding.imagePartner.setImageBitmap(result)
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                            Log.d(TAG, "onResourceCleared")
                        }

                    })
            }

        }
    }

    private inner class ViewHolderAudioMe(val binding:ItemVoiceMeBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data:MessageAudio){
            val reactionMe = data.react.first()
            val reactionPartner = data.react.last()
            if (reactionMe.count != 0) {
                binding.apply {
                    cardViewReactionOne.VISIBLE()
                    imgReactionOne.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionMe.react))
                    tvCountReactionOne.text = reactionMe.count.toString()
                }
            } else binding.cardViewReactionOne.GONE()
            if (reactionPartner.count != 0) {
                binding.apply {
                    cardViewReactionTwo.VISIBLE()
                    imgReactionTwo.setImageResource(UiHelper.getSymbolReactionImageByPosition(reactionPartner.react))
                    tvCountReactionTwo.text = reactionPartner.count.toString()
                }
            } else binding.cardViewReactionTwo.GONE()
            binding.tvDurationAudio.text = "[ ${data.duration?.div(1000)}'' ]"
            binding.ibAudio.setImageResource(if(!data.isPlaying) R.drawable.ic_play else R.drawable.ic_pause)
        }
    }


    private var onItemImageClicked: ((View, String) -> Unit)? = null

    fun setOnItemImageClickListener(listener: (View, String) -> Unit) {
        onItemImageClicked = listener
    }

    override fun getItemViewType(position: Int): Int {
        val item = differ.currentList[position]
        return if (item.isMe) {
            when (item.type) {
                MessageType.TEXT -> Constants.TEXT_FROM_ME
                MessageType.IMAGE -> Constants.IMAGE_FROM_ME
                else -> Constants.AUDIO_FROM_ME
            }
        } else {
            when (item.type) {
                MessageType.TEXT -> Constants.TEXT_FROM_PARTNER
                MessageType.IMAGE -> Constants.IMAGE_FROM_PARTNER
                else -> Constants.AUDIO_FROM_PARTNER
            }
        }
    }

    private val diffUtilItemCallBack = object : DiffUtil.ItemCallback<MessageModel>() {

        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }
    val differ = AsyncListDiffer(this, diffUtilItemCallBack)

    fun submitList(list: List<MessageModel>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view: View = layoutInflater.inflate(R.layout.item_chat_me, parent, false)
        var vh: RecyclerView.ViewHolder = ViewHolderMe(ItemChatMeBinding.bind(view))
        when (viewType) {
            Constants.TEXT_FROM_PARTNER -> {
                view = layoutInflater.inflate(R.layout.item_chat_partner, parent, false)
                vh = ViewHolderPartner(ItemChatPartnerBinding.bind(view))
            }
            Constants.IMAGE_FROM_ME -> {
                view = layoutInflater.inflate(R.layout.item_chat_image_me, parent, false)
                vh = ViewHolderImageMe(ItemChatImageMeBinding.bind(view))
            }
            Constants.IMAGE_FROM_PARTNER -> {
                view = layoutInflater.inflate(R.layout.item_chat_image_partner, parent, false)
                vh = ViewHolderImagePartner(ItemChatImagePartnerBinding.bind(view))
            }
            Constants.AUDIO_FROM_ME -> {
            view = layoutInflater.inflate(R.layout.item_voice_me, parent, false)
            vh = ViewHolderAudioMe(ItemVoiceMeBinding.bind(view))
        }
        }
        return vh
    }

    fun getItemAtPosition(position: Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        when (holder) {
            is ViewHolderMe -> {
                holder.bind(item as MessageText)
                val binding = holder.binding
                handleEventTextMe(binding.root.context,binding){reaction->
                    handleReaction(binding.cardViewReactionOne,binding.cardViewReactionTwo, item,reaction)
                    notifyItemChanged(position)
                }
            }
            is ViewHolderPartner -> {
                holder.bind(item as MessageText)
                val binding = holder.binding
                handleEventTextPartner(binding.root.context, binding) { reaction ->
                    handleReaction(binding.cardViewReactionOne,binding.cardViewReactionTwo, item,reaction)
                    notifyItemChanged(position)
                }
            }
            is ViewHolderImageMe -> {
                val itemResult = item as MessageImage
                holder.bind(itemResult)
                val binding = holder.binding
                handleEventImageMe(binding.root.context, binding,itemResult.urlImage) { reaction ->
                    handleReaction(binding.cardViewReactionOne,binding.cardViewReactionTwo, item,reaction)
                    notifyItemChanged(position)
                }
            }
            is ViewHolderImagePartner -> {
                val itemResult = item as MessageImage
                holder.bind(itemResult)
                val binding = holder.binding
                handleEventImagePartner(binding.root.context, binding,itemResult.urlImage) { reaction ->
                    handleReaction(binding.cardViewReactionOne,binding.cardViewReactionTwo, item,reaction)
                    notifyItemChanged(position)
                }
            }
            is ViewHolderAudioMe->{
                val itemResult = item as MessageAudio
                holder.bind(itemResult)
                val binding = holder.binding
                handleEventVoiceMe(binding.root.context,binding,itemResult.isPlaying,{
                    reaction->
                    handleReaction(binding.cardViewReactionOne,binding.cardViewReactionTwo, item,reaction)
                    notifyItemChanged(position)
                }){
                    item.isPlaying = it
                    if(it)
                    notifyItemChanged(position)
                    onAudioClick?.let { it(position,itemResult) }
                }
            }
        }
    }

    private var onAudioClick:((Int,MessageAudio)->Unit?)?=null
    fun setAudioClickListener(listener:(Int,MessageAudio)->Unit){
        onAudioClick = listener
    }

    private fun handleReaction(cardOne:View,cardTwo:View,itemResult:MessageModel,reaction: Int) {
        if(reaction!=-1){
            if (itemResult.isMe&&itemResult.react[0].react==reaction) {
                cardOne.VISIBLE()
                val count = itemResult.react.first().count+1
                val item = itemResult.react.first()
                item.count = count
            }else if(!itemResult.isMe&&itemResult.react[1].react==reaction) {
                cardTwo.VISIBLE()
                val count = itemResult.react.last().count+1
                val item = itemResult.react.last()
                item.count = count
            } else if(itemResult.isMe){
                val item = itemResult.react.first()
                item.count = 1
                item.react = reaction
            } else {
                val item = itemResult.react.last()
                item.count = 1
                item.react = reaction
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEventTextMe(
        context: Context,
        binding: ItemChatMeBinding,
        callBack: (Int) -> Unit
    ) {
        val popup = ReactionPopup(
            context,
            UiHelper.configReactionPopUp(context, true),
            object : ReactionSelectedListener {
                override fun invoke(position: Int): Boolean {
                            callBack(position)
                    Log.d(TAG, "invoke: $position")
                    return true
                }

            })
        binding.cardViewMessageMe.setOnTouchListener { view, event ->
            popup.onTouch(view, event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (binding.tvTimeMe.isVisible) {
                        binding.tvTimeMe.GONE()
                    } else binding.tvTimeMe.VISIBLE()
                }
            }
            true
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun handleEventTextPartner(
        context: Context,
        binding: ItemChatPartnerBinding,
        callBack: (Int) -> Unit
    ) {
        val popup = ReactionPopup(
            context,
            UiHelper.configReactionPopUp(context, true),
            object : ReactionSelectedListener {
                override fun invoke(position: Int): Boolean {
                    callBack(position)
                    Log.d(TAG, "invoke: $position")
                    return true
                }

            })
        binding.cardViewMessagePartner.setOnTouchListener { view, event ->
            popup.onTouch(view, event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (binding.tvTimePartner.isVisible) {
                        binding.tvTimePartner.GONE()
                    } else binding.tvTimePartner.VISIBLE()
                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEventImageMe(
        context: Context,
        binding: ItemChatImageMeBinding,
        urlImage:String,
        callBack: (Int) -> Unit
    ) {
        val popup = ReactionPopup(
            context,
            UiHelper.configReactionPopUp(context, true),
            object : ReactionSelectedListener {
                override fun invoke(position: Int): Boolean {
                    callBack(position)
                    Log.d(TAG, "invoke: $position")
                    return true
                }

            })
        binding.cardViewImageMe.setOnTouchListener { view, event ->
            popup.onTouch(view, event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (binding.tvTimeMe.isVisible) {
                        binding.tvTimeMe.GONE()
                    } else binding.tvTimeMe.VISIBLE()
                }
            }
            true
        }
        binding.lyEye.setOnClickListener {
            onItemImageClicked?.let {
                it(binding.imageMe, urlImage)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEventImagePartner(
        context: Context,
        binding: ItemChatImagePartnerBinding,
        urlImage:String,
        callBack: (Int) -> Unit
    ) {
        val popup = ReactionPopup(
            context,
            UiHelper.configReactionPopUp(context, true),
            object : ReactionSelectedListener {
                override fun invoke(position: Int): Boolean {
                    callBack(position)
                    Log.d(TAG, "invoke: $position")
                    return true
                }

            })
        binding.cardViewImagePartner.setOnTouchListener { view, event ->
            popup.onTouch(view, event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (binding.tvTimePartner.isVisible) {
                        binding.tvTimePartner.GONE()
                    } else binding.tvTimePartner.VISIBLE()
                }
            }
            true
        }
        binding.lyEye.setOnClickListener {
            onItemImageClicked?.let {
                it(binding.imagePartner, urlImage)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEventVoiceMe(
        context: Context,
        binding: ItemVoiceMeBinding,
        isPlaying:Boolean,
        callBack: (Int) -> Unit,
        callBackAudio: (Boolean) -> Unit,
    ) {
        val popup = ReactionPopup(
            context,
            UiHelper.configReactionPopUp(context, true),
            object : ReactionSelectedListener {
                override fun invoke(position: Int): Boolean {
                    callBack(position)
                    Log.d(TAG, "invoke: $position")
                    return true
                }

            })
        binding.cardViewAudioMe.setOnTouchListener { view, event ->
            popup.onTouch(view, event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (binding.tvTimeMe.isVisible) {
                        binding.tvTimeMe.GONE()
                    } else binding.tvTimeMe.VISIBLE()
                }
            }
            true
        }
        binding.ibAudio.setOnClickListener {
            if(isPlaying){
                binding.ibAudio.setImageResource(R.drawable.ic_play)
                callBackAudio(false)
            } else {
                callBackAudio(true)
                binding.ibAudio.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}