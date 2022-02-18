package com.devapp.fr.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.R
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.ItemOnlineChatBinding
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.animation.Animation

import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toInvisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener


class AccountOnlineAdapter(
    private val fragment:Fragment
    ,private val realTimeViewModel: RealTimeViewModel): RecyclerView.Adapter<AccountOnlineAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemOnlineChatBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserProfile){
            user.images?.get(0)?.let { GlideApp.loadImage(it,binding.imgFriend,fragment) }
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            binding.dotOnline.startAnimation(anim)
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<UserProfile>(){

        override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<UserProfile>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOnlineChatBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItemAtPosition(position)
        holder.bind(item)
        holder.binding.imgFriend.setOnClickWithAnimationListener {
            onClickItemListener?.let { it(holder.binding.imgFriend,item) }
        }
        holder.binding.imgFriend.setOnLongClickListener {
            holder.binding.root.showSnackbar("${item.name} đó ~")
            true
        }
        realTimeViewModel.checkUserOnOffbyId(item.id,{
            if(it) {
                holder.binding.apply {
                    imgFriend.borderColor = ContextCompat.getColor(root.context,R.color.blue)
                    lyDot.toVisible()
                }
            } else {
                holder.binding.apply {
                    imgFriend.borderColor = ContextCompat.getColor(root.context,R.color.gray)
                    lyDot.toGone()
                }
            }
        })
    }

    private var onClickItemListener:((View, UserProfile)->Unit)?=null

    fun setOnItemClickListener(listener:(View,UserProfile)->Unit){
        onClickItemListener= listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}