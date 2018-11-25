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
            Constants.MESSAGE_WHAT_POSTMSG -> {
                val message: Message = Message();
                message.what = 0;
                val bundle = Bundle();
                bundle.putSerializable(Constants.KEY_EVENT,
                        msg.data.getSerializable(Constants.KEY_EVENT));
                message.data = bundle
                serviceMap.forEach { t: String, u: Messenger ->

                    u.send(message)
                }
                true
            }
            Constants.MESSAGE_WHAT_REGISTER -> {
                register(msg.data)
                true
            }
            else -> false
        }
    })

    override fun onBind(intent: Intent?): IBinder {
        return mMessenger.binder;
    }

    private fun register(bundle: Bundle) {
        val messenger: Messenger = bundle.getParcelable(Constants.KEY_MESSENGER);
        val packageName = bundle.getString(Constants.KEY_PID);
        if (!serviceMap.contains(packageName)) {
            serviceMap.put(packageName, messenger);
        }
    }


}