package com.proficiency.assingment.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proficiency.assingment.R
import com.proficiency.assingment.model.FactsResponse
import com.proficiency.assingment.util.Constants.Companion.NO_DEC
import com.proficiency.assingment.util.Constants.Companion.NO_TITLE
import kotlinx.android.synthetic.main.item_facts.view.*

class FactsAdapter : RecyclerView.Adapter<FactsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallBack = object : DiffUtil.ItemCallback<FactsResponse.Row>() {
        override fun areItemsTheSame(
            oldItem: FactsResponse.Row,
            newItem: FactsResponse.Row
        ): Boolean {
            return oldItem.imageHref == newItem.imageHref
        }

        override fun areContentsTheSame(
            oldItem: FactsResponse.Row,
            newItem: FactsResponse.Row
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_facts,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((FactsResponse.Row) -> Unit)? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val facts = differ.currentList[position]
        holder.itemView.apply {

            if(!TextUtils.isEmpty(facts.title)) {
                tvTitle.text = facts.title
            } else{
                tvTitle.text = NO_TITLE
            }
            if(!TextUtils.isEmpty(facts.description)) {
                tvDetails.text = facts.description
            } else{
                tvDetails.text = NO_DEC
            }
            Glide.with(this).load(facts.imageHref).placeholder(R.drawable.placeholder).into(imageView)

            setOnClickListener {
                onItemClickListener?.let { it(facts) }
            }
        }
    }

    fun setOnItemClickListener(listener: (FactsResponse.Row) -> Unit) {
        onItemClickListener = listener
    }

}