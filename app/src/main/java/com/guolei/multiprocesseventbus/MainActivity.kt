package com.guolei.multiprocesseventbus

import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        findViewById<Button>(R.id.start_second).setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(message: Any) {
        Log.e("a", "main receive data")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
