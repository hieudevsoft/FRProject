package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.R
import com.devapp.fr.data.models.items.InformationItem
import com.devapp.fr.databinding.ItemInformationBinding


class InformationAdapter : RecyclerView.Adapter<InformationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: InformationItem) {
            val title = data.title
            val content = data.content
            binding.apply {
                tvHeader.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(
                            binding.root.resources,
                            data.icon,
                            null
                        ), null, null, null
                    )
                    text = title
                }
                tvFooter.text = if(content.isEmpty()){
                    tvFooter.setTextColor(ResourcesCompat.getColor(
                        binding.root.resources,
                        R.color.color_blue_500,
                        null
                    ))
                    "Thêm"
                }else{
                    tvFooter.setTextColor(ResourcesCompat.getColor(
                        binding.root.resources,
                        R.color.color_grey,
                        null
                    ))
                    title
                }
            }
        }
    }

    private val diffUtilItemCallBack = object : DiffUtil.ItemCallback<InformationItem>() {

        override fun areItemsTheSame(oldItem: InformationItem, newItem: InformationItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: InformationItem,
            newItem: InformationItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffUtilItemCallBack)

    fun submitList(list: List<InformationItem>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemInformationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}