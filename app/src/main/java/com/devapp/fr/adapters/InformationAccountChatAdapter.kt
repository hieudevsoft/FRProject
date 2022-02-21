package com.devapp.fr.adapters

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.R
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.ItemInformationChatBinding
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import java.util.*


class InformationAccountChatAdapter(private val idUser:String,private val fragment:Fragment,private val realTimeViewModel: RealTimeViewModel) : RecyclerView.Adapter<InformationAccountChatAdapter.ViewHolder>() {

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
        holder.binding.apply{
            root.setOnClickListener {
                onClickItemListener?.let { it(item) }
            }
            cardMessageNew.setOnClickWithAnimationListener {
                tvLastMsg.isSelected = true
                if(tvLastMsg.isVisible) {
                    tvLastMsg.toGone()
                    tvTimeLastMessage.toGone()
                } else {
                    tvLastMsg.toVisible()
                    tvTimeLastMessage.toVisible()
                }
            }
            realTimeViewModel.getLastMessage(idUser+item.id){
                if(it.split("#").size==2){
                    cardMessageNew.toVisible()
                    tvLastMsg.text = it.split("#")[1]
                    tvTimeLastMessage.text = it.split("#")[0].split(",")[1]
                } else{
                    cardMessageNew.toGone()
                }
                realTimeViewModel.getCombineOldAndNewSeen(idUser+item.id){
                    try {
                        val oldSize = if(it.split("#")[1].contains("null",true)) 0 else it.split("#")[1].toInt()
                        val newSize = it.split("#")[0].toInt()
                        val numDiff = newSize-oldSize
                        if(numDiff<=0){
                            tvNumberNotSeen.text = ">"
                            tvNumberNotSeen.rotation = 90f
                            tvLastMsg.setTypeface(null,Typeface.NORMAL)
                            tvLastMsg.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.background_dark_mode))
                            cardTvLastTime.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(), R.color.color_white_500))
                            tvTimeLastMessage.setTypeface(null,Typeface.NORMAL)
                        }else{
                            tvNumberNotSeen.text = numDiff.toString()
                            tvLastMsg.setTypeface(null,Typeface.BOLD)
                            tvTimeLastMessage.setTypeface(null,Typeface.BOLD)
                            tvLastMsg.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.white))
                            cardTvLastTime.setCardBackgroundColor(ContextCompat.getColor(fragment.requireContext(), R.color.background_dark_mode))

                        }
                    }catch (e:Exception){
                        cardMessageNew.toGone()
                        tvTimeLastMessage.toGone()
                        tvLastMsg.toGone()
                    }
                }
            }
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