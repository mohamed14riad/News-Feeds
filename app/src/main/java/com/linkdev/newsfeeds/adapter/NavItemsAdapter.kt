package com.linkdev.newsfeeds.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkdev.newsfeeds.R
import com.linkdev.newsfeeds.data.model.NavItem
import com.linkdev.newsfeeds.databinding.NavItemBinding

class NavItemsAdapter(
    private val context: Context,
    private val navItems: List<NavItem>,
    private val itemClickListener: OnNavItemClickListener,
) : RecyclerView.Adapter<NavItemsAdapter.NavItemHolder>() {

    private var lastSelectedPosition = 0

    fun selectItem(position: Int) {
        if (!navItems[position].isSelected) {
            navItems[position].isSelected = true
            notifyItemChanged(position)

            if (position != lastSelectedPosition) {
                navItems[lastSelectedPosition].isSelected = false
                notifyItemChanged(lastSelectedPosition)
            }

            lastSelectedPosition = position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemHolder {
        val itemBinding: NavItemBinding =
            NavItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return NavItemHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: NavItemHolder, position: Int) {
        val navItem = navItems[position]

        Glide.with(context)
            .load(navItem.icon)
            .into(viewHolder.itemBinding.navItemIcon)

        viewHolder.itemBinding.navItemTitle.text = navItem.title

        if (navItem.isSelected) {
            viewHolder.itemBinding.navItemSelectedView.setBackgroundResource(R.drawable.selected)
        } else {
            viewHolder.itemBinding.navItemSelectedView.setBackgroundResource(android.R.color.transparent)
        }
    }

    override fun getItemCount(): Int {
        return navItems.size
    }

    inner class NavItemHolder(val itemBinding: NavItemBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ), View.OnClickListener {

        init {
            itemBinding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition

            selectItem(position)

            itemClickListener.onNavItemClick(position, navItems[position])
        }
    }

    interface OnNavItemClickListener {
        fun onNavItemClick(position: Int, navItem: NavItem)
    }
}