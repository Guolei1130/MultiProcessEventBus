package com.guolei.lib_multiprocess

/**
 * Created by Android Studio.
 * User: guolei
 * Email: 1120832563@qq.com
 * Date: 2018/11/25
 * Time: 12:28 PM
 * Desc:
 */
class Constants {
    companion object {
        @JvmField
        val MESSAGE_WHAT_POSTMSG = 0
        @JvmField
        val MESSAGE_WHAT_REGISTER = 1
        @JvmField
        val MESSAGE_WHAT_REVEIVE_MSG = 3

        @JvmField
        val KEY_EVENT = "event"
        @JvmField
        val KEY_MESSENGER = "messenger"
        @JvmField
        val KEY_PID = "pid"
    }
}