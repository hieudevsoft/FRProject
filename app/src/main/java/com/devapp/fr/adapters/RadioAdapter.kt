package com.devapp.fr.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Index
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.LayoutItemRadioBinding
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log


class RadioAdapter(private val onCheckedChange: (Int, Boolean) -> Unit) :
    RecyclerView.Adapter<RadioAdapter.ViewHolder>() {
    private var listItemRadio = emptyList<RadioItem>()
    inner class ViewHolder(val binding: LayoutItemRadioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RadioItem) {
            binding.radio.apply {
                isChecked = item.isChecked
                text = item.text
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<RadioItem>) {
        listItemRadio = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemRadioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemRadio[position]
        holder.bind(item)
        holder.binding.radio.setOnClickWithAnimationListener {
            getItemAtPosition(position).isChecked = !getItemAtPosition(position).isChecked
            if(getItemAtPosition(position).isChecked) {
                notifyItemChanged(position)
                onCheckedChange(position,getItemAtPosition(position).isChecked)
                listItemRadio.forEachIndexed { index, radioItem ->
                    if(index!=position) radioItem.isChecked=false
                    notifyItemChanged(index)
                }
            }

        }
    }

    fun setItemChecked(position: Int) {
        getItemAtPosition(position).isChecked = true
        notifyItemChanged(position)
    }

    private fun getItemAtPosition(pos:Int) = listItemRadio[pos]

    override fun getItemCount(): Int {
        return listItemRadio.size
    }
}