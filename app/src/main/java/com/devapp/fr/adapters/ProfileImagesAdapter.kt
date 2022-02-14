package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.databinding.ItemImageProfileBinding
import androidx.fragment.app.Fragment
import com.devapp.fr.R
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener


class ProfileImagesAdapter(private val fragment:Fragment) : RecyclerView.Adapter<ProfileImagesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemImageProfileBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: String){
            GlideApp.loadImage(data,binding.imgInsideCard,fragment)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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