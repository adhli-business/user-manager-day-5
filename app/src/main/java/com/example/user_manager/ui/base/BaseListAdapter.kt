package com.example.user_manager.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindItem(holder, getItem(position))
    }

    abstract fun bindItem(holder: VH, item: T)
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
}
