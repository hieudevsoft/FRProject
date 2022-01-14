package com.devapp.fr.adapters


import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.models.items.InterestItem
import com.devapp.fr.databinding.ItemTagInterestBinding


class InterestAdapter : RecyclerView.Adapter<InterestAdapter.ViewHolder>() {
    val listIndexSelected = mutableListOf<Int>()
    inner class ViewHolder(val binding: ItemTagInterestBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: InterestItem){
            if(item.isSelected) ViewCompat.setBackgroundTintList(binding.root,
                item.itemBgColorSelected?.let { ColorStateList.valueOf(it) })
            else ViewCompat.setBackgroundTintList(binding.root,
                item.itemBgColorDefault.let { ColorStateList.valueOf(it) })
            binding.tvInterest.text = item.itemName
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<InterestItem>(){

        override fun areItemsTheSame(oldItem: InterestItem, newItem: InterestItem): Boolean {
            return oldItem.itemName == newItem.itemName
        }

        override fun areContentsTheSame(oldItem: InterestItem, newItem: InterestItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<InterestItem>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTagInterestBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            if(item.isSelected) listIndexSelected.remove(position)
            else listIndexSelected.add(position)
            item.isSelected = !item.isSelected
            notifyItemChanged(position)
        }
    }



    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}