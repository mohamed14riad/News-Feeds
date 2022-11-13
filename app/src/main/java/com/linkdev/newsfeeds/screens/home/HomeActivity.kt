package com.linkdev.newsfeeds.screens.home

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkdev.newsfeeds.R
import com.linkdev.newsfeeds.adapter.NavItemsAdapter
import com.linkdev.newsfeeds.data.model.NavItem
import com.linkdev.newsfeeds.databinding.ActivityHomeBinding
import com.linkdev.newsfeeds.screens.base_view.BaseActivity
import com.linkdev.newsfeeds.screens.home.explore.NewsFragment
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class HomeActivity : BaseActivity(),
    NavItemsAdapter.OnNavItemClickListener {

    companion object {
        private const val TAG = "HomeAct"
    }

    private lateinit var viewBinding: ActivityHomeBinding

    private lateinit var navItems: List<NavItem>
    private lateinit var navItemsAdapter: NavItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.icMenu.setOnClickListener {
            viewBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        navItems = listOf(
            NavItem(1, R.drawable.explore, getString(R.string.explore), true),
            NavItem(2, R.drawable.live_chat, getString(R.string.live_chat), false),
            NavItem(3, R.drawable.gallery, getString(R.string.gallery), false),
            NavItem(4, R.drawable.wishlist, getString(R.string.wishlist), false),
            NavItem(5, R.drawable.e_magazine, getString(R.string.e_magazine), false),
        )
        navItemsAdapter = NavItemsAdapter(this, navItems, this)
        viewBinding.rvNavItems.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        viewBinding.rvNavItems.adapter = navItemsAdapter


        selectFragment(0)
    }

    private fun selectFragment(position: Int) {
        when (position) {
            0 -> {
//                viewBinding.fragmentContainer.visibility = View.VISIBLE

                supportFragmentManager.beginTransaction()
                    .replace(viewBinding.fragmentContainer.id, NewsFragment())
                    .commit()
            }
            else -> {
//                viewBinding.fragmentContainer.visibility = View.GONE

                Toasty.info(
                    this,
                    navItems[position].title,
                    Toasty.LENGTH_SHORT,
                    true
                ).show()
            }
        }
    }

    override fun onNavItemClick(position: Int, navItem: NavItem) {
        viewBinding.drawerLayout.closeDrawer(GravityCompat.START)

        selectFragment(position)
    }
}