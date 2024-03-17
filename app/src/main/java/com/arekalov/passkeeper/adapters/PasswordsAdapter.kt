package com.arekalov.passkeeper.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arekalov.data.db.Password
import com.arekalov.passkeeper.R
import com.arekalov.passkeeper.databinding.PasswordCardBinding
import com.bumptech.glide.Glide
import kotlin.math.log

class PasswordsAdapter : RecyclerView.Adapter<PasswordsAdapter.VIewHolder>() {
    inner class VIewHolder(val binding: PasswordCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Password>() {
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this, diffUtil)
    var onCLick: ((Password) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIewHolder {
        return VIewHolder(
            PasswordCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VIewHolder, position: Int) {
        val pass = differ.currentList[position]
        holder.binding.tvName.text = pass.name
        holder.binding.tvLogin.text = pass.login
        holder.binding.tvUrl.text = pass.url

        Glide.with(holder.itemView)
            .load(pass.url + "/favicon.ico")
            .placeholder(R.drawable.img_no_image)
            .into(holder.binding.ivFavicon)


        holder.itemView.setOnClickListener {
            onCLick!!.invoke(differ.currentList[position])
        }
    }

}