package com.linkdev.newsfeeds.data.model

data class NavItem(
    val id: Int,
    val icon: Int,
    val title: String,
    var isSelected: Boolean,
)