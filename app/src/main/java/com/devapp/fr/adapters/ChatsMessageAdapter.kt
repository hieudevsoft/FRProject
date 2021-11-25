package com.devapp.fr.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.R
import com.devapp.fr.data.models.items.Message
import com.devapp.fr.databinding.ItemChatMeBinding
import com.devapp.fr.databinding.ItemChatPartnerBinding
import com.devapp.fr.util.Constants.FROM_ME
import com.devapp.fr.util.Constants.FROM_PARTNER
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE


class ChatsMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolderMe(val binding: ItemChatMeBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: Message){
            var isClicked = false
            binding.tvTimeMe.GONE()
            binding.tvTimeMe.text = data.time
            binding.tvMessageMe.text = data.message
            binding.cardViewMessageMe.setOnClickListener {
                isClicked=!isClicked
                if(isClicked) binding.tvTimeMe.VISIBLE() else binding.tvTimeMe.GONE()
            }
        }
    }
    private inner class ViewHolderPartner(val binding: ItemChatPartnerBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(data: Message){
                var isClicked = false
                binding.tvTimePartner.GONE()
                binding.tvTimePartner.text = data.time
                binding.tvMessagePartner.text = data.message
                binding.cardViewMessagePartner.setOnClickListener {
                    isClicked=!isClicked
                    if(isClicked) binding.tvTimePartner.VISIBLE() else binding.tvTimePartner.GONE()
                }
            }
    }

    override fun getItemViewType(position: Int): Int {
        return if(differ.currentList[position].isMe){
            FROM_ME
        }else{
            FROM_PARTNER
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<Message>(){

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<Message>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view:View = layoutInflater.inflate(R.layout.item_chat_me,parent,false)
        var vh:RecyclerView.ViewHolder = ViewHolderMe(ItemChatMeBinding.bind(view))
        if(viewType== FROM_PARTNER){
            view = layoutInflater.inflate(R.layout.item_chat_partner,parent,false)
            vh = ViewHolderPartner(ItemChatPartnerBinding.bind(view))
        }
        return vh
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        if(holder is ViewHolderMe){
            holder.bind(item)
        }else if(holder is ViewHolderPartner){
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}