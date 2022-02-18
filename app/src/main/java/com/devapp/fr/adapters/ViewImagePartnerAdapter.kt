package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.databinding.LayoutItemImageViewProfileBinding
import com.devapp.fr.ui.activities.ViewPartnerActivity
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener


class ViewImagePartnerAdapter(private val fragment: ViewPartnerActivity) : RecyclerView.Adapter<ViewImagePartnerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutItemImageViewProfileBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: String){
            GlideApp.loadImage(data,binding.imgInsideCard, fragment)
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<String>(){

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<String>){
        differ.submitList(list)
    }

    fun getCurrentList() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutItemImageViewProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.binding.apply {
            imgInsideCard.setOnClickWithAnimationListener {view->
                onItemClickListener?.let { it(view,item) }
            }
        }

    }
    private var onItemClickListener:((View,String)->Unit)?=null

    fun setOnItemClickListener(listener:(View,String)->Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}