package com.zippyyum.media

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.zippyyum.media.repository.MediaRepository
import kotlinx.android.synthetic.main.activity_main.*
import com.zippyyum.media.adapter.PagerAdapter
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.zippyyum.media.utilities.Helper


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_toolbar.title = ""
        setSupportActionBar(main_toolbar)

        initApplication()

    }

    private fun initApplication() {
        if (Helper.checkPermissions(this))
            readMediaItemsNames()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initApplication()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Helper.checkPermissions(this))
            initApplication()
    }

    private fun readMediaItemsNames() {
        val mediaItems = MediaRepository(this).getMediaModels()
        for (mediaItem in mediaItems) {
            tab_layout?.addTab(tab_layout?.newTab()!!.setText(mediaItem.name))
        }
        val adapter = PagerAdapter(supportFragmentManager, mediaItems)
        pager.adapter = adapter

        pager.adapter = adapter
        pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                pager.currentItem = position
            }
        })

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(selectedTab: TabLayout.Tab) {
                pager?.currentItem = selectedTab.position
                for (i in 0 until tab_layout.tabCount) {
                    val tab = tab_layout.getTabAt(i)
                    if (selectedTab.position == tab?.position) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            selectedTab.customView?.findViewById<TextView>(R.id.tab_title)?.setTextColor(resources.getColor(R.color.orange, null))
                        } else {
                            selectedTab.customView?.findViewById<TextView>(R.id.tab_title)?.setTextColor(resources.getColor(R.color.orange))
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tab?.customView?.findViewById<TextView>(R.id.tab_title)?.setTextColor(resources.getColor(R.color.colorAccent, null))
                        } else {
                            tab?.customView?.findViewById<TextView>(R.id.tab_title)?.setTextColor(resources.getColor(R.color.colorAccent))
                        }
                    }
                }
            }
        })




        for (i in 0 until tab_layout.tabCount) {
            val tab = tab_layout.getTabAt(i)
            val layout = LayoutInflater.from(this).inflate(R.layout.tab_layout, tab_layout, false) as RelativeLayout

            val tabTextView = layout.findViewById(R.id.tab_title) as TextView
            tabTextView.text = tab?.text
            tab?.customView = layout
        }

        if (tab_layout.tabCount > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tab_layout.getTabAt(0)?.customView?.findViewById<TextView>(R.id.tab_title)?.setTextColor(resources.getColor(R.color.orange, null))
            } else {
                tab_layout.getTabAt(0)?.customView?.findViewById<TextView>(R.id.tab_title)?.setTextColor(resources.getColor(R.color.orange))
            }
        }
    }
}
