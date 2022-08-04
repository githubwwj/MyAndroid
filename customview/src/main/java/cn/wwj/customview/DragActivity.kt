package cn.wwj.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.widget.DragView
import cn.wwj.customview.widget.WaveView

/**
 * 波浪Activity
 */
class DragActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_view)
        val drawView = findViewById<DragView>(R.id.mydrag)
        drawView.initLocation(true)

        drawView.setDragViewListener(object : DragView.DragViewListener {
            override fun onClickImg() {

            }

            override fun onClickClose() {

            }
        })
    }
}