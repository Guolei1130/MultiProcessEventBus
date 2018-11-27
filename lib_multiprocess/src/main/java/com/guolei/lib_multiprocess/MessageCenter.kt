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
        val message = Message();
        val bundle = Bundle();
        if (msg.data.getSerializable(Constants.KEY_EVENT) != null) {
            bundle.putSerializable(Constants.KEY_EVENT,
                    msg.data.getSerializable(Constants.KEY_EVENT));
        }
        if (msg.data.getParcelable<Parcelable>(Constants.KEY_EVENT) != null) {
            bundle.putParcelable(Constants.KEY_EVENT,
                    message.data.getParcelable<Parcelable>(Constants.KEY_EVENT))
        }
        message.data = bundle

        if (msg.what == Constants.MESSAGE_WHAT_REGISTER) {
            register(msg.data)
        } else {
            message.what = msg.what
            serviceMap.forEach { t: String, u: Messenger ->
                u.send(message)
            }
        }
        true

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