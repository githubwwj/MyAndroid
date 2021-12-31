package cn.wwj.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent(this, IntentFilterActivity::class.java)
        val intent = Intent("com.test.intentfilter")
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
        startActivity(intent)
    }
}