package com.spacey.myhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.spacey.myhome.todo.TodoFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.home_drawer)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val homeFragment = HomeFragment()
                    homeFragment.arguments = bundleOf(HomeFragment.SAMPLE_STRING_KEY to "Home")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, homeFragment).commit()
                }

                R.id.nav_todo -> {
                    val todoFragment = TodoFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, todoFragment)
                        .addToBackStack("TodoFragment").commit()
                }

                else -> {}
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}