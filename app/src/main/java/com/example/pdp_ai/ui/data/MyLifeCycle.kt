package com.example.pdp_ai.ui.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyLifeCycle(private val context: Context, private val myLifeCycle: Lifecycle): LifecycleObserver  {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.i("MyLifeCycleLog", "ON_CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.i("MyLifeCycleLog", "ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.i("MyLifeCycleLog", "ON_RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.i("MyLifeCycleLog", "ON_PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.i("MyLifeCycleLog", "ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.i("MyLifeCycleLog", "ON_DESTROY")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny () {
        Log.i("MyLifeCycleLog", "ON_ANY")
    }

}
