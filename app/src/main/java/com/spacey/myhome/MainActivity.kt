package com.spacey.myhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.home_drawer)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            val fragmentBundle = Bundle()
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    fragmentBundle.putString(HomeFragment.SAMPLE_STRING_KEY, "Home")
                }
                R.id.nav_milk -> {
                    fragmentBundle.putString(HomeFragment.SAMPLE_STRING_KEY, "Milk packets")
                }
                R.id.nav_water -> {
                    fragmentBundle.putString(HomeFragment.SAMPLE_STRING_KEY, "Water cans")
                }
                else -> {}
            }
            val homeFragment = HomeFragment()
            homeFragment.arguments = fragmentBundle
            supportFragmentManager.beginTransaction().replace(R.id.home_fragment_container, homeFragment).commit()
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}