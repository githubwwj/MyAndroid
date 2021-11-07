package cn.wwj.mytouchevent

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.wwj.mytouchevent.databinding.ActivityLinearTouchEventBinding
import cn.wwj.mytouchevent.databinding.ActivityViewpaygerTouchEventBinding
import com.google.android.material.tabs.TabLayout


class ViewPagerTouchEventActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityViewpaygerTouchEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewPager: ViewPager = binding.myViewPager
        val tabLayout: TabLayout = binding.tabLayout

        val list = listOf<Fragment>(OneFragment(), OneFragment(), OneFragment())
        val tabFragmentPagerAdapter = TabFragmentPagerAdapter(supportFragmentManager, list)
        viewPager.adapter = tabFragmentPagerAdapter

        tabLayout.setTabTextColors(Color.WHITE, Color.RED)
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"))
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"))
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"))
        tabLayout.setupWithViewPager(viewPager)
    }
}