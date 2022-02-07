package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.databinding.LayoutItemCardBinding
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener

class CardStackViewAdapter(
    private val onCancelCallBack:((View)->Unit)?=null,
    private val onLoveCallBack:((View)->Unit)?=null,

):RecyclerView.Adapter<CardStackViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:LayoutItemCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:String) {

        }
    }

    private val  diffUtilCallBack = object:DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    fun submitList(list:List<String>){
        differ.submitList(list)
    }

    private val differ = AsyncListDiffer(this,diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackViewAdapter.ViewHolder {
        return ViewHolder(LayoutItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPostion(position: Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: CardStackViewAdapter.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.binding.apply {
            btnClose.setOnClickWithAnimationListener {view->
                onCancelCallBack?.let { it(view) }
            }


            btnLove.setOnClickWithAnimationListener {view->
                onLoveCallBack?.let { it(view) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}