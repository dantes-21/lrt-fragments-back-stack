package com.dantes.lrtbackstack.manager

import android.support.v4.app.Fragment

interface FragmentStackManager {

    fun changeStack(stackName: String)

    fun getCurrentStackName(): String

    fun getStackSize(stackName: String?): Int

    fun isStackEmpty(stackName: String?): Boolean

    fun pushFragment(fragment: Fragment, tag: String)

    fun peekFragment(): Fragment?

    fun popFragment(): Fragment?

    fun popBackTo(tag: String): Fragment?

    fun onBackPressed(): Boolean

    fun openRootFragment(stack: String, fragment: Fragment, tag: String)

}