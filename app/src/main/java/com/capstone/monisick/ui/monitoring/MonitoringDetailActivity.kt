package com.capstone.monisick.ui.monitoring

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstone.monisick.R
import com.capstone.monisick.data.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class MonitoringDetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring_detail)
        val tvTitleToolbar = findViewById<TextView>(R.id.tvTitleToolbar)
        tvTitleToolbar.text = "Monitor Detail"
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        val monitoringItem = intent.getParcelableExtra<Parcelable>("monitoring_item")
        intent.putExtra("monitoring_item", monitoringItem)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Obat"
                1 -> "Makan"
                2 -> "Log"
                else -> null
            }
        }.attach()
        val selectedTab = intent.getIntExtra("selected_tab", 0)
        viewPager.setCurrentItem(selectedTab, false)
    }
}