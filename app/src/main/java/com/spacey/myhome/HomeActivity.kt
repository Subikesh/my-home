package com.spacey.myhome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.snackbar.Snackbar
import com.spacey.myhome.databinding.ActivityHomeBinding
import com.spacey.myhome.dateservices.DateServicesFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        activeFragment = DateServicesFragment()

        loadFragment(activeFragment, "dateServices")

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> loadFragment(DateServicesFragment(), "dateServices")
                R.id.services -> loadFragment(SecondFragment(), "secondFragment")
                else -> {}
            }
            true
        }
        supportFragmentManager.addOnBackStackChangedListener {
            updateBottomNavSelection()
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.commit {
            val existingFragment = supportFragmentManager.findFragmentByTag(tag)

            if (existingFragment != null) {
                hide(activeFragment)
                show(existingFragment)
            } else {
                hide(activeFragment)
                if (tag == "dateServices") {
                    add(R.id.content_home, fragment, tag)
                } else {
                    replace(R.id.content_home, fragment, tag)
                    addToBackStack(tag)
                }
            }
            setReorderingAllowed(true)
        }
        activeFragment = fragment
    }

    fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    private fun updateBottomNavSelection() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.content_home)

        when (currentFragment) {
            is DateServicesFragment -> binding.bottomNavigation.selectedItemId = R.id.home
            is SecondFragment -> binding.bottomNavigation.selectedItemId = R.id.services
            else -> {}
        }
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}