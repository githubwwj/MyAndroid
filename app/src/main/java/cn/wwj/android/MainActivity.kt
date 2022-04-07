package cn.wwj.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent(this, IntentFilterActivity::class.java)
//        val intent = Intent("com.test.intentfilter")
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        startActivity(intent)

        val intent2 = Intent(this, TestService::class.java)
        bindService(intent2, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val iMathService = IMathService.Stub.asInterface(service)
            val result = iMathService.add(10, 20)
            Log.d("tag", "--------result=$result")
            Toast.makeText(this@MainActivity, "result=$result", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

}