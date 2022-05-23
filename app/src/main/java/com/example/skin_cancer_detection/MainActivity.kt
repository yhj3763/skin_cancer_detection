package com.example.skin_cancer_detection

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Button
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment






class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.host,HomeFragment(),"HOME")
            .commit()

        setToolbar()

        navigationView.setNavigationItemSelectedListener(this)

    }



    private fun setToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.menu_about -> startActivity(Intent(this@MainActivity,AboutActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {  // 네비게이션 메뉴가 클릭되면 스낵바가 나타난다.

            R.id.detection -> replaceFragment(DetectionFragment())
            R.id.definition-> replaceFragment(InformationFragment1())
            R.id.types->replaceFragment(InformationFragment2())
            R.id.cause->replaceFragment(InformationFragment3())
            R.id.prevention->replaceFragment(InformationFragment4())
            R.id.hospital ->  startActivity(Intent(this@MainActivity,MapsActivity::class.java))
            R.id.report -> startActivity(Intent(this@MainActivity,ReportActivity::class.java))

        }

        drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.host,fragment)
            .commit()

    }


}