package com.devapp.fr.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.databinding.ItemColorInboxBinding


class ColorInBoxAdapter : RecyclerView.Adapter<ColorInBoxAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemColorInboxBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(color: String){
            binding.root.setCardBackgroundColor(Color.parseColor(color))
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
        return ViewHolder(ItemColorInboxBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItemAtPosition(position)
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            onLickItemListener?.let { it(item) }
        }
    }

    private var onLickItemListener:((String)->Unit)?=null

    fun setOnItemClickListener(listener:(String)->Unit){
        onLickItemListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}