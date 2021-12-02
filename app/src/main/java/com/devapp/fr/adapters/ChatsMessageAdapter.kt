package com.devapp.fr.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import com.devapp.fr.R
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.MessageImage
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.data.models.messages.MessageText
import com.devapp.fr.databinding.ItemChatImageMeBinding
import com.devapp.fr.databinding.ItemChatImagePartnerBinding
import com.devapp.fr.databinding.ItemChatMeBinding
import com.devapp.fr.databinding.ItemChatPartnerBinding
import com.devapp.fr.util.Constants
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE
import com.devapp.fr.util.UiHelper.convertDpToPixel
import com.devapp.fr.util.UiHelper.convertPixelsToDp
import kotlin.math.ceil
import kotlin.math.roundToInt


class ChatsMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = "ChatsMessageAdapter"
    private inner class ViewHolderMe(val binding: ItemChatMeBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: MessageText){
            var isClicked = false
            binding.tvTimeMe.GONE()
            binding.tvTimeMe.text = data.time
            binding.tvMessageMe.text = data.message
            binding.cardViewMessageMe.setOnClickListener {
                isClicked=!isClicked
                if(isClicked) binding.tvTimeMe.VISIBLE() else binding.tvTimeMe.GONE()
            }
        }
    }
    private inner class ViewHolderPartner(val binding: ItemChatPartnerBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(data: MessageText){
                var isClicked = false
                binding.tvTimePartner.GONE()
                binding.tvTimePartner.text = data.time
                binding.tvMessagePartner.text = data.message
                binding.cardViewMessagePartner.setOnClickListener {
                    isClicked=!isClicked
                    if(isClicked) binding.tvTimePartner.VISIBLE() else binding.tvTimePartner.GONE()
                }
            }
    }
    private inner class ViewHolderImageMe(val binding: ItemChatImageMeBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: MessageImage){
            var isClicked = false
            binding.tvTimeMe.GONE()
            binding.tvTimeMe.text = data.time

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
                    .into(object:CustomViewTarget<ImageView,Bitmap>(binding.imageMe){
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            Log.d(TAG, "onLoadFailed: error load $data.id")
                        }
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val maxSize = binding.root.context.resources.getDimension(R.dimen.MAX_SIZE_IMAGE_MASSAGE)
                            val result = UiHelper.scaleDown(resource,maxSize,true)
                            Log.d(TAG, "onResourceReady: ${resource.width} ${resource.height} ${result?.width} ${result?.height}" )
                            binding.imageMe.setImageBitmap(result)
                            binding.imageMe.isDrawingCacheEnabled = true
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {

                        }

                    })
            }

            binding.cardViewImageMe.setOnLongClickListener {
                isClicked=!isClicked
                if(isClicked) binding.tvTimeMe.VISIBLE() else binding.tvTimeMe.GONE()
                true
            }

            binding.cardViewImageMe.setOnClickListener {
                onItemImageClicked?.let {
                    it(binding.imageMe,data.urlImage)
                }
            }
        }
    }

    private inner class ViewHolderImagePartner(val binding: ItemChatImagePartnerBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: MessageImage){
            var isClicked = false
            binding.tvTimePartner.GONE()
            binding.tvTimePartner.text = data.time

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
                    .into(object:CustomViewTarget<ImageView,Bitmap>(binding.imagePartner){
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            Log.d(TAG, "onLoadFailed: error load $data.id")
                        }
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val maxSize = binding.root.context.resources.getDimension(R.dimen.MAX_SIZE_IMAGE_MASSAGE)
                            val result = UiHelper.scaleDown(resource,maxSize,true)
                            Log.d(TAG, "onResourceReady: ${resource.width} ${resource.height} ${result?.width} ${result?.height}" )
                            binding.imagePartner.setImageBitmap(result)
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                            Log.d(TAG, "onResourceCleared")
                        }

                    })
            }

            binding.cardViewImagePartner.setOnLongClickListener {
                isClicked=!isClicked
                if(isClicked) binding.tvTimePartner.VISIBLE() else binding.tvTimePartner.GONE()
                true
            }

            binding.cardViewImagePartner.setOnClickListener {
                onItemImageClicked?.let {
                    it(binding.imagePartner,data.urlImage)
                }
            }
        }
    }


    private var onItemImageClicked:((View,String)->Unit)?=null

    fun setOnItemImageClickListener(listener:(View,String)->Unit){
        onItemImageClicked = listener
    }

    override fun getItemViewType(position: Int): Int {
        val item  = differ.currentList[position]
        return if(item.isMe){
            when (item.type) {
                MessageType.TEXT -> Constants.TEXT_FROM_ME
                MessageType.IMAGE -> Constants.IMAGE_FROM_ME
                else -> Constants.AUDIO_FROM_ME
            }
        }else{
            when (item.type) {
                MessageType.TEXT -> Constants.TEXT_FROM_PARTNER
                MessageType.IMAGE -> Constants.IMAGE_FROM_PARTNER
                else -> Constants.AUDIO_FROM_PARTNER
            }
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<MessageModel>(){

        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<MessageModel>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: $parent")
        val layoutInflater = LayoutInflater.from(parent.context)
        var view:View = layoutInflater.inflate(R.layout.item_chat_me,parent,false)
        var vh:RecyclerView.ViewHolder = ViewHolderMe(ItemChatMeBinding.bind(view))
        when (viewType) {
            Constants.TEXT_FROM_PARTNER -> {
                view = layoutInflater.inflate(R.layout.item_chat_partner,parent,false)
                vh = ViewHolderPartner(ItemChatPartnerBinding.bind(view))
            }
            Constants.IMAGE_FROM_ME -> {
                view = layoutInflater.inflate(R.layout.item_chat_image_me,parent,false)
                vh = ViewHolderImageMe(ItemChatImageMeBinding.bind(view))
            }
            Constants.IMAGE_FROM_PARTNER -> {
                view = layoutInflater.inflate(R.layout.item_chat_image_partner, parent, false)
                vh = ViewHolderImagePartner(ItemChatImagePartnerBinding.bind(view))
            }
        }
        return vh
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        when (holder) {
            is ViewHolderMe -> {
                holder.bind(item as MessageText)
            }
            is ViewHolderPartner -> {
                holder.bind(item as MessageText)
            }
            is ViewHolderImageMe -> {
                holder.bind(item as MessageImage)
            }
            is ViewHolderImagePartner -> {
                holder.bind(item as MessageImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}