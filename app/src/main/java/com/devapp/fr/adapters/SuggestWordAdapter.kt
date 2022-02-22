package com.devapp.fr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.R
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener

class SuggestWordAdapter(val listener:(String)->Unit):
    RecyclerView.Adapter<SuggestWordAdapter.MyViewHolder>() {
    private var listWords:List<String> = emptyList()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mSuggestWord: TextView = itemView.findViewById(R.id.tvSuggestWords)
        fun setData(string:String){
            mSuggestWord.text = string
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.ly_suggest_word,parent,false)
                return MyViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(listWords[position])
        holder.itemView.setOnClickWithAnimationListener {
            listener(holder.mSuggestWord.text.toString())
        }
    }
    fun setData(newList:List<String>){
        val diffUtil = DiffUtilSuggestWord(listWords,newList)
        val diffUtlCallbackResult = DiffUtil.calculateDiff(diffUtil)
        listWords = newList
        diffUtlCallbackResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return listWords.size
    }
}

class DiffUtilSuggestWord(private var oldList:List<String>, private var newList: List<String>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]===newList[newItemPosition]
    }
}