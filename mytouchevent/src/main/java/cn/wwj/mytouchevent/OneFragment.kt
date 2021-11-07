package cn.wwj.mytouchevent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment


class OneFragment : Fragment() {

    private lateinit var mRootView: View
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::mRootView.isInitialized) {
            mRootView = inflater.inflate(R.layout.fragment_one, null)
            val recyclerView: ListView = mRootView.findViewById(R.id.listView)
            recyclerView.adapter = MyAdapter(mContext)
        }
        return mRootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    class MyAdapter(val mContext: Context) : BaseAdapter() {

        override fun getCount(): Int {
            return 8
        }

        override fun getItem(position: Int): Any {
            return 0
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_simple_tv, null)
            val tvItem: TextView = view.findViewById(R.id.tv_item)
            tvItem.text = "0$position"
            return view
        }

    }

}