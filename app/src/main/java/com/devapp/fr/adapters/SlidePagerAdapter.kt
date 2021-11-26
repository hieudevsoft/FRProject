package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.models.items.SlideItem
import com.devapp.fr.databinding.ItemLayoutSlideBinding

class SlidePagerAdapter(
    private val listItem:List<SlideItem>,
    private val textColor:Int
): RecyclerView.Adapter<SlidePagerAdapter.PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(ItemLayoutSlideBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        val binding = holder.binding
        val item = listItem[position]

        binding.title.apply {
            text = item.title
            setTextColor(textColor)
        }
        binding.description.apply {
            text = item.des
            setTextColor(textColor)
        }

        binding.imageView.setImageResource(item.image)
    }

    class PagerVH(val binding: ItemLayoutSlideBinding) : RecyclerView.ViewHolder(binding.root)
}
