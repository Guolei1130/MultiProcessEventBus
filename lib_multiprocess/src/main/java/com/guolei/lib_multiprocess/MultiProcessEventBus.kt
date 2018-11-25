package com.guolei.lib_multiprocess

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.io.Serializable

/**
 * Created by Android Studio.
 * User: guolei
 * Email: 1120832563@qq.com
 * Date: 2018/11/24
 * Time: 8:34 PM
 * Desc:
 */
class MultiProcessEventBus private constructor() {


    companion object {
        val instance: MultiProcessEventBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MultiProcessEventBus()
        }
    }

    fun register(context: Context) {
        val intent = Intent(context, MessageCenter::class.java)
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun post(event: Any) {
        val message: Message = Message.obtain()
        message.what = 0
        if (event is Serializable) {
            val bundle = Bundle()
            bundle.putSerializable(Constants.KEY_EVENT, event)
            message.data = bundle
        }
        mServiceMessenger.send(message)
    }


    lateinit var mServiceMessenger: Messenger

    var clientMessenger: Messenger = Messenger(Handler { msg ->
        when (msg.what) {
            0 -> {
                Log.e("a", "receive message and ->" + getCurrentPID())
                if (msg.data != null) {
                    EventBus.getDefault().post(msg.data.getSerializable("event"))
                }
                true
            }
            else -> true
        }
    })

    private var mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mServiceMessenger = Messenger(service)
            val msg = Message.obtain()
            val bundle = Bundle()
            bundle.putParcelable(Constants.KEY_MESSENGER, clientMessenger)
            bundle.putString(Constants.KEY_PID, getCurrentPID())
            msg.data = bundle
            msg.what = 1
            mServiceMessenger.send(msg)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // do nothing
        }
    }


    fun getCurrentPID(): String {
        return "pid" + Process.myPid()
    }
}