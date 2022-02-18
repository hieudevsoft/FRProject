package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.ItemInformationChatBinding
import com.devapp.fr.util.GlideApp
import java.util.*


class InformationAccountChatAdapter(private val fragment:Fragment) : RecyclerView.Adapter<InformationAccountChatAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInformationChatBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserProfile){
            binding.apply {
                GlideApp.loadImage(user.images?.get(0)!!,imgAvatar,fragment)
                bio.text = user.bio
                val age = Calendar.getInstance().get(Calendar.YEAR)-user.dob.split("/").last().toInt()+1
                tvNameAge.text = "${user.name}, $age"
                val gender = if(user.gender==0) "Nữ" else "Nam"
                tvGenderAddress.text = "${gender}, ${user.address}"
            }
        }
    }

    private val diffUtilItemCallBack = object:DiffUtil.ItemCallback<UserProfile>(){

        override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<UserProfile>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemInformationChatBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun getItemAtPosition(position:Int) = differ.currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItemAtPosition(position)
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            onClickItemListener?.let { it(item) }
        }
    }

    private var onClickItemListener:((UserProfile)->Unit)?=null

    fun setOnItemClickListener(listener:(UserProfile)->Unit){
        onClickItemListener= listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}