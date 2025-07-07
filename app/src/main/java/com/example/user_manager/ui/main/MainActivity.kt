package com.example.user_manager.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.user_manager.R
import com.example.user_manager.databinding.ActivityMainBinding
import com.example.user_manager.ui.base.BaseActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupActionBar()
        observeNavigationEvents()
    }

    private fun setupNavigation() {
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_user_list,
                R.id.nav_search,
                R.id.nav_statistics,
                R.id.nav_profile,
                R.id.nav_deleted
            ),
            drawerLayout
        )

        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun observeNavigationEvents() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Update toolbar title or handle specific destination actions
            when (destination.id) {
                R.id.nav_user_list -> binding.toolbar.title = getString(R.string.nav_users)
                R.id.nav_search -> binding.toolbar.title = getString(R.string.nav_search)
                R.id.nav_statistics -> binding.toolbar.title = getString(R.string.nav_statistics)
                R.id.nav_profile -> binding.toolbar.title = getString(R.string.nav_profile)
                R.id.nav_deleted -> binding.toolbar.title = getString(R.string.nav_deleted)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_user_list -> navController.navigate(R.id.nav_user_list)
            R.id.nav_search -> navController.navigate(R.id.nav_search)
            R.id.nav_statistics -> navController.navigate(R.id.nav_statistics)
            R.id.nav_profile -> navController.navigate(R.id.nav_profile)
            R.id.nav_deleted -> navController.navigate(R.id.nav_deleted)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
