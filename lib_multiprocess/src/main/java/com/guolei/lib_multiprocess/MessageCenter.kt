package com.guolei.lib_multiprocess

import android.app.Service
import android.content.Intent
import android.os.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Android Studio.
 * User: guolei
 * Email: 1120832563@qq.com
 * Date: 2018/11/24
 * Time: 9:39 PM
 * Desc:
 */
class MessageCenter : Service() {

    var serviceMap: ConcurrentHashMap<String, Messenger> = ConcurrentHashMap();

    val mMessenger: Messenger = Messenger(Handler() { msg ->
        when (msg.what) {
            0 -> {
//                val message: Any = msg.obj;
                val message: Message = Message();
                message.what = 0;
                val bundle = Bundle();
                bundle.putSerializable("event", msg.data.getSerializable("event"));
                message.data = bundle
                serviceMap.forEach { t: String, u: Messenger ->

                    u.send(message)
                }
                true
            }
            1 -> {
                register(msg.data)
                true
            }
            else -> false
        }
    })

    override fun onBind(intent: Intent?): IBinder {
//        register(intent)
        return mMessenger.binder;
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun register(bundle: Bundle) {
        val messenger: Messenger = bundle.getParcelable("messenger");
        val packageName = bundle.getString("pid");
        if (!serviceMap.contains(packageName)) {
            serviceMap.put(packageName, messenger);
        }
    }


}