package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.models.RadioModel
import com.devapp.fr.databinding.LayoutItemRadioBinding


class RadioAdapter(private val onCheckedChange:(Int,Boolean)->Unit): RecyclerView.Adapter<RadioAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutItemRadioBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: RadioModel){
            binding.radio.apply {
                isChecked= item.isChecked
                text = item.text
            }
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<RadioModel>(){

        override fun areItemsTheSame(oldItem: RadioModel, newItem: RadioModel): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: RadioModel, newItem: RadioModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<RadioModel>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutItemRadioBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.binding.radio.setOnCheckedChangeListener { _, b ->
            onCheckedChange(position,b)
            if (b) resetStateRadio(position)
        }
    }

    private fun resetStateRadio(position: Int){
        differ.currentList.forEachIndexed { index, radioModel ->
            if(position!=index) {
                radioModel.isChecked = false
                notifyItemChanged(index)
            }

        }
    }

    fun setItemChecked(position:Int){
        differ.currentList[position].isChecked = true
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}