package cn.wwj.customview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.widget.NodePointProcessBar
import cn.wwj.customview.widget.WaveView

/**
 * 节点进度Activity
 */
class NodeProgressBarActivity : AppCompatActivity() {

    private val mTextList: List<String> = mutableListOf("提交申请", "商家处理", "寄回商品", "商家退款", "退款成功")
    private var mSelectedIndexSet: Set<Int> = mutableSetOf(0, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_node_progress_bar)
        val nodePointPb: NodePointProcessBar = findViewById(R.id.nodePointPb)

        Handler(Looper.getMainLooper()).postDelayed({
            nodePointPb.show(mTextList, mSelectedIndexSet)
        }, 1000)
    }
}