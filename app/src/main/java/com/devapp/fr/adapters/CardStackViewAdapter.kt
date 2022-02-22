package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.LayoutItemCardBinding
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import java.util.*
import androidx.fragment.app.Fragment

class CardStackViewAdapter(
    private val fr:Fragment,
    private val onCancelClickListener:((View)->Unit)?=null,
    private val onMatchClickListener:((View)->Unit)?=null,
    private val onRootClickListener:((View,UserProfile)->Unit)?=null,
    private val onRootCardLongClickListener:((View)->Unit)?=null,
):RecyclerView.Adapter<CardStackViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:LayoutItemCardBinding):RecyclerView.ViewHolder(binding.root){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackViewAdapter.ViewHolder {
        return ViewHolder(LayoutItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPostion(position: Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: CardStackViewAdapter.ViewHolder, position: Int) {
        val item = getItemAtPostion(position)
        holder.bind(item)
        holder.binding.apply {
            btnClose.setOnClickWithAnimationListener {view->
                onCancelClickListener?.let { it(view) }
            }

            root.setOnLongClickListener { view->
                onRootCardLongClickListener?.let { it(view) }
                true
            }

            root.setOnClickWithAnimationListener { view->
                onRootClickListener?.let { it(imgAvatarProfile,item) }
            }


            btnLove.setOnClickWithAnimationListener {view->
                onMatchClickListener?.let { it(view) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}