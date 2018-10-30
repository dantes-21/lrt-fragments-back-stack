package com.dantes.lrtbackstack.listeners

import android.os.Bundle

interface ActivityLifecycleListener {

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onSavedInstanceState(outInstanceState: Bundle?)

    fun onRestoreInstanceState(savedInstanceState: Bundle?)

    fun onActivityStart()

    fun onActivityResume()

    fun onActivityPause()

}