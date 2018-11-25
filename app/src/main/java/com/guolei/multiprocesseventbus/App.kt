package com.guolei.multiprocesseventbus

import android.app.Application
import com.guolei.lib_multiprocess.MultiProcessEventBus

/**
 * Created by Android Studio.
 * User: guolei
 * Email: 1120832563@qq.com
 * Date: 2018/11/25
 * Time: 10:04 AM
 * Desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiProcessEventBus.instance.register(this)
    }
}