package cn.wwj.mytouchevent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.mytouchevent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnStartLL.setOnClickListener {
            startActivity(Intent(this,LinearLayoutTouchEventActivity::class.java))
        }

        binding.btnStartViewPager.setOnClickListener {
            startActivity(Intent(this,ViewPagerTouchEventActivity::class.java))
        }
    }
}