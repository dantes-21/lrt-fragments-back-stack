package com.dantes.lrtbackstack

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dantes.lrtbackstack.listeners.ActivityLifecycleListener
import com.dantes.lrtbackstack.manager.FragmentStackManager
import com.dantes.lrtbackstack.listeners.StackChangeListener

abstract class StackAppCompatActivity : AppCompatActivity(), StackChangeListener {

    abstract fun getFragmentStackManager(): FragmentStackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (getFragmentStackManager() as ActivityLifecycleListener).onActivityCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        (getFragmentStackManager() as ActivityLifecycleListener).onSavedInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        (getFragmentStackManager() as ActivityLifecycleListener).onSavedInstanceState(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        (getFragmentStackManager() as ActivityLifecycleListener).onActivityStart()
    }

    override fun onResume() {
        super.onResume()
        (getFragmentStackManager() as ActivityLifecycleListener).onActivityResume()
    }

    override fun onPause() {
        super.onPause()
        (getFragmentStackManager() as ActivityLifecycleListener).onActivityPause()
    }
}