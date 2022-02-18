package com.devapp.fr.adapters

import android.animation.Animator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.data.models.Member
import com.devapp.fr.databinding.ItemCardVisitBinding
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder

class AboutUsAdapter : RecyclerView.Adapter<AboutUsAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemCardVisitBinding): RecyclerView.ViewHolder(binding.root),
        AnimateViewHolder {
        fun bind(member: Member){
            binding.data = member
            binding.imgFront.setBackgroundResource(member.imageFront)
            binding.lottieAnimation.setAnimation(member.lottieAnimationBack)
            binding.executePendingBindings()
        }

        override fun animateAddImpl(
            holder: RecyclerView.ViewHolder,
            listener: Animator.AnimatorListener
        ) {
            itemView.animate().apply {
                translationY(0f)
                alpha(1f)
                duration = 300
                setListener(listener)
            }.start()
        }

        override fun animateRemoveImpl(
            holder: RecyclerView.ViewHolder,
            listener: Animator.AnimatorListener
        ) {
            itemView.animate().apply {
                translationY(-itemView.height * 0.3f)
                alpha(0f)
                duration = 300
                setListener(listener)
            }.start()
        }

        override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
            itemView.setTranslationY(-itemView.height * 0.3f)
            itemView.setAlpha(0f)
        }

        override fun preAnimateRemoveImpl(holder: RecyclerView.ViewHolder) {

        }
    }

    private val diffUtilItemCallBack = object: DiffUtil.ItemCallback<Member>(){
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtilItemCallBack)

    fun submitList(list:List<Member>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCardVisitBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.binding.cardFront.setOnClickListener {
            holder.binding.flipView.flipTheView(true)
        }
        holder.binding.cardBack.setOnClickListener {
            holder.binding.flipView.flipTheView(true)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}