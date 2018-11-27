package com.guolei.lib_multiprocess

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.io.Serializable

@Suppress("unused")
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
        val bundle = Bundle()
        when (event) {
            is Serializable -> bundle.putSerializable(Constants.KEY_EVENT, event)
            is Parcelable -> bundle.putParcelable(Constants.KEY_EVENT, event)
            else -> throw IllegalArgumentException("event must be serializeable or parcelable")
        }
        message.data = bundle
        mServiceMessenger.send(message)
    }

    fun postSticky(event: Any) {
        val message: Message = Message.obtain()
        message.what = Constants.MESSAGE_WHAT_POST_STICKY
        val bundle = Bundle()
        when (event) {
            is Serializable -> bundle.putSerializable(Constants.KEY_EVENT, event)
            is Parcelable -> bundle.putParcelable(Constants.KEY_EVENT, event)
            else -> throw IllegalArgumentException("event must be serializable or parcelable")
        }
        message.data = bundle
        mServiceMessenger.send(message)
    }

    fun removeAllSticky() {
        val message = Message.obtain()
        message.what = Constants.MESSAGE_WHAT_REMOVE_ALL_STICKY
        mServiceMessenger.send(message)
    }

    fun removeStickyEvent(eventType: Class<*>) {
        val message = Message.obtain()
        message.what = Constants.MESSAGE_WHAT_REMOVE_STICKY_BY_CLASS
        val bundle = Bundle()
        bundle.putSerializable(Constants.KEY_EVENT, eventType)
        mServiceMessenger.send(message)
    }

    fun removeStickyEvent(event: Any) {
        val message: Message = Message.obtain()
        message.what = Constants.MESSAGE_WHAT_REMOVE_STICKY_BY_OBJECT
        val bundle = Bundle()
        when (event) {
            is Serializable -> bundle.putSerializable(Constants.KEY_EVENT, event)
            is Parcelable -> bundle.putParcelable(Constants.KEY_EVENT, event)
            else -> throw IllegalArgumentException("event must be serializable or parcelable")
        }
        message.data = bundle
        mServiceMessenger.send(message)
    }

    lateinit var mServiceMessenger: Messenger

    var clientMessenger: Messenger = Messenger(Handler { msg ->
        when (msg.what) {
            Constants.MESSAGE_WHAT_POSTMSG -> {
                Log.e("a", "receive message and ->" + getCurrentPID())
                if (msg.data != null) {
                    EventBus.getDefault().post(msg.data.getSerializable(Constants.KEY_EVENT))
                }
                true
            }
            Constants.MESSAGE_WHAT_POST_STICKY -> {
                if (msg.data != null) {
                    EventBus.getDefault().postSticky(msg.data.getSerializable(Constants.KEY_EVENT))
                }
                true
            }
            Constants.MESSAGE_WHAT_REMOVE_ALL_STICKY -> {
                EventBus.getDefault().removeAllStickyEvents()
                true
            }
            Constants.MESSAGE_WHAT_REMOVE_STICKY_BY_OBJECT -> {
                if (msg.data != null) {
                    val event = msg.data.getSerializable(Constants.KEY_EVENT);
                    if (event != null) {
                        EventBus.getDefault().removeStickyEvent(event)
                    }
                    val eventP = msg.data.getParcelable<Parcelable>(Constants.KEY_EVENT)
                    if (eventP != null) {
                        EventBus.getDefault().removeStickyEvent(eventP)
                    }
                }
                true
            }
            Constants.MESSAGE_WHAT_REMOVE_STICKY_BY_CLASS -> {
                if (msg.data != null) {
                    val eventType: Class<*> = msg.data.getSerializable(Constants.KEY_EVENT) as Class<*>;
                    EventBus.getDefault().removeStickyEvent(eventType)
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