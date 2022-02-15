package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.ItemNotificationsMatchBinding
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import java.util.*
import androidx.fragment.app.Fragment

class NotificationsMatchAdapter(
    private val fr:Fragment,
    private val onCancelClickListener:((View,Int)->Unit)?=null,
    private val onMatchClickListener:((View,Int)->Unit)?=null,
    private val onRootClickListener:((View,UserProfile)->Unit)?=null,
):RecyclerView.Adapter<NotificationsMatchAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:ItemNotificationsMatchBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:UserProfile) {
            binding.apply {
                val age = Calendar.getInstance().get(Calendar.YEAR)-(item.dob).split("/").last().toInt()+1
                tvOtherName.text = "${item.name}, $age"
                val gender = if(item.gender==0) "Nữ" else "Nam"
                tvAddress.text = "${gender}, ${item.address}"
                GlideApp.loadImage(item.images!!.shuffled()[0],imgAvatarProfile,fr)
            }
        }
    }

    private val  diffUtilCallBack = object:DiffUtil.ItemCallback<UserProfile>(){
        override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    fun submitList(list:List<UserProfile>){
        differ.submitList(list)
    }

    private val differ = AsyncListDiffer(this,diffUtilCallBack)
    fun getListCurrent() = differ.currentList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsMatchAdapter.ViewHolder {
        return ViewHolder(ItemNotificationsMatchBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPostion(position: Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: NotificationsMatchAdapter.ViewHolder, position: Int) {
        val item = getItemAtPostion(position)
        holder.bind(item)
        holder.binding.apply {
            btnClose.setOnClickWithAnimationListener {view->
                onCancelClickListener?.let { it(view,position) }
            }


            root.setOnClickWithAnimationListener { view->
                onRootClickListener?.let { it(view,item) }
            }


            btnLove.setOnClickWithAnimationListener {view->
                onMatchClickListener?.let { it(view,position) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}