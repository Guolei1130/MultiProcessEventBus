package com.guolei.multiprocesseventbus

import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.guolei.lib_multiprocess.MultiProcessEventBus
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Android Studio.
 * User: guolei
 * Email: 1120832563@qq.com
 * Date: 2018/11/24
 * Time: 10:31 PM
 * Desc:
 */
class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        findViewById<Button>(R.id.postmessage).setOnClickListener {
            MultiProcessEventBus.instance.post(TestMessage())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(message: Any) {
        Log.e("a", "second receive event ")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}