package com.example.mobileapplicationassignment

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView

class MainMenu : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView : NavigationView
    private lateinit var actionBarDrawerToggle : ActionBarDrawerToggle
    private lateinit var myToolbar: Toolbar

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)


        val id = intent.getStringExtra("Id").toString()



        myToolbar = findViewById(R.id.myToolbar)
        myToolbar.title = "Drawer menu"
        setSupportActionBar(myToolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigationView)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        navigationView.setNavigationItemSelectedListener{
                menuItem ->
            when(menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    //replaceFragment()
//you action.....

                    true
                }R.id.nav_profile ->{
                    drawerLayout.closeDrawer(GravityCompat.START)
                    val fragment = ProfileFragment()
                    val bundle = Bundle()
                    bundle.putString("id",id)
                    fragment.arguments = bundle

                    replaceFragment(fragment)

                    true
                }R.id.nav_cart ->{
                drawerLayout.closeDrawer(GravityCompat.START)
                val fragment = ShoppingCartFragment()
                val bundle = Bundle()
                bundle.putString("id",id)
                fragment.arguments = bundle

                replaceFragment(fragment)

                true
            }R.id.nav_favourite ->{
                drawerLayout.closeDrawer(GravityCompat.START)
                val fragment = FavoriteProductFragment()
                val bundle = Bundle()
                bundle.putString("id",id)
                fragment.arguments = bundle

                replaceFragment(fragment)

                true
            }

                else ->{
                false
            }
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }


}