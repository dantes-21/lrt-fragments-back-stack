package com.dantes.lrtbackstack.manager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.dantes.lrtbackstack.listeners.ActivityLifecycleListener
import com.dantes.lrtbackstack.exceptions.NoSuchStackException
import com.dantes.lrtbackstack.listeners.FragmentShowListener
import com.dantes.lrtbackstack.listeners.StackChangeListener
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * The Fragment Stack Manager implementation. This class allow manipulate stacks and fragments
 * inside it. The manager implements lifecycle listener because the information about stacks
 * saves and restores using in the Saved Instance State.
 *
 * You have to use the StackAppCompatActivity or manually call the Lifecycle functions
 * in appropriate methods.
 */
internal class FragmentStackManagerImpl(activity: AppCompatActivity) : FragmentStackManager, ActivityLifecycleListener {
    private val mFragmentTransactions = ArrayDeque<DeferredFragmentTransaction>()
    private val mFragmentManager = activity.supportFragmentManager

    private var mFragmentsStacks = LinkedHashMap<String, LinkedList<String>>()
    private var mCurrentStack: String? = "default"
    private var mActivityRunning = true

    internal var stackChangeListener: StackChangeListener? = null
    internal var fragmentShowListener: FragmentShowListener? = null
    internal var useStacksHistory = false
    internal var fragmentsContainer = 0

    companion object {
        private const val STACK_DATA = "lrt.back.stack.data"
    }

    /**
     * It should be called in activity onCreate method.
     * Restores the stacks states if it exist in activity bundle
     */
    override fun onActivityCreate(savedInstanceState: Bundle?) {
        onRestoreInstanceState(savedInstanceState)
    }

    /**
     * It should be called in activity onSavedInstanceState method.
     * Method saves the current Stacks states
     */
    override fun onSavedInstanceState(outInstanceState: Bundle?) {
        outInstanceState?.putSerializable(STACK_DATA, mFragmentsStacks)
    }

    /**
     * It should be called in activity onRestoreInstanceState method.
     * Method restores the Stacks states
     */
    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            if(it.getSerializable(STACK_DATA) != null) {
                mFragmentsStacks = it.getSerializable(STACK_DATA) as LinkedHashMap<String, LinkedList<String>>
                if(mFragmentsStacks.keys.isNotEmpty()) {
                    mCurrentStack = mFragmentsStacks.keys.last()
                }
            }
        }
    }

    /**
     * It should be called in activity onStart method.
     */
    override fun onActivityStart() {
        while (mFragmentTransactions.isNotEmpty()) {
            mFragmentTransactions.remove().commit()
        }
    }

    /**
     * It should be called in activity onResume method.
     */
    override fun onActivityResume() {
        mActivityRunning = true
    }

    /**
     * It should be called in activity onPause method.
     */
    override fun onActivityPause() {
        mActivityRunning = false
    }

    /**
     * Change the current stack. When stack is not exist in the Map it will be created.
     * If we use the stack history, we also have to sort the current Map and put new Stack
     * to the last position
     *
     * @param stackName - the name of the stack
     */
    override fun changeStack(stackName: String) {
        mCurrentStack = stackName
        stackChangeListener?.onStackChanged(stackName)
        if(mFragmentsStacks.isEmpty() || mFragmentsStacks.keys.find { it == stackName } == null) {
            mFragmentsStacks.put(stackName, LinkedList())
        } else if(!mFragmentsStacks.isEmpty() && mFragmentsStacks.keys.toList()[mFragmentsStacks.keys.toList().size - 1] != stackName && useStacksHistory) {
            sortCurrentStacks(stackName)
        }
    }

    /**
     * Return the stack size
     *
     * @param stackName - the name of the stack
     * @return the selected stack size
     */
    override fun getStackSize(stackName: String?): Int {
        if(mFragmentsStacks.isEmpty()) return 0
        mFragmentsStacks[stackName]?.let { return it.size }
        throw NoSuchStackException(stackName)
    }

    /**
     * Simple function to check the stack emptiness
     *
     * @param stackName - the name of the stack
     * @return true if the stack is empty, false otherwise
     */
    override fun isStackEmpty(stackName: String?): Boolean {
        if(mFragmentsStacks.isEmpty()) return true
        mFragmentsStacks[stackName]?.let { return it.isEmpty() }
        throw NoSuchStackException(stackName)
    }

    /**
     * Add fragment to the current stack using tag and add it in the fragment container.
     * The transaction will be added as deferred transaction to prevent the IllegalStateException
     *
     * @param fragment - new fragment
     * @param tag - the fragment tag
     */
    override fun pushFragment(fragment: Fragment, tag: String) {
        if(mFragmentsStacks.isEmpty() && mCurrentStack != null) {
            mFragmentsStacks.put(mCurrentStack!!, LinkedList())
        }
        pushTagToStack(tag)
        openFragment(fragment, tag)
    }

    /**
     * Get a top fragment from the current stack and show it
     *
     * @return if operation is successfully return fragment that was shown, null otherwise
     */
    override fun peekFragment(): Fragment? {
        if(mFragmentsStacks[mCurrentStack] != null && !mFragmentsStacks[mCurrentStack]!!.isEmpty()) {
            mFragmentsStacks[mCurrentStack]?.peek()?.let {
                val fragment = mFragmentManager.findFragmentByTag(it) ?: return null
                openFragment(fragment, it)
                return fragment
            }
        }
        return null
    }

    /**
     * Get a top fragment from the current stack, show it and remove from the stack
     *
     * @return if operation is successfully return fragment that was shown, null otherwise
     */
    override fun popFragment(): Fragment? {
        if(mFragmentsStacks[mCurrentStack] != null && !mFragmentsStacks[mCurrentStack]!!.isEmpty()) {
            mFragmentsStacks[mCurrentStack]?.pop()?.let {
                val fragment = mFragmentManager.findFragmentByTag(it) ?: return null
                openFragment(fragment, it)
                return fragment
            }
        }
        return null
    }

    /**
     * Go back to some fragment in the current stack if its exist
     *
     * @param tag - the tag of the fragment to which we will move
     * @return fragment that will be shown after the method is executed, null if there is no
     *         fragment in the stack with this tag
     */
    override fun popBackTo(tag: String): Fragment? {
        if(mFragmentsStacks[mCurrentStack] == null || mFragmentsStacks[mCurrentStack]!!.find { it == tag }.isNullOrEmpty())
            return null
        do {
            val fr = mFragmentsStacks[mCurrentStack]!!.peek()
            if(fr != tag) mFragmentsStacks[mCurrentStack]!!.pop()
        } while (fr != tag)
        return peekFragment()
    }

    /**
     * It may be called in the onBackPressed method in the activity class to simplify the
     * work with stacks when user want to go back from current fragment
     *
     * @return true if some fragment was shown, false if the stack is empty. When you get
     *         false you may to call super.onBackPressed() or whatever you want to handle the
     *         end of the fragments stacks
     */
    override fun onBackPressed(): Boolean {
        if(getStackSize(mCurrentStack) > 1) {
            mFragmentsStacks[mCurrentStack]?.pop()
            return peekFragment() != null
        } else if(getStackSize(mCurrentStack) == 1 && useStacksHistory) {
            mFragmentsStacks[mCurrentStack]?.pop()
            getPreviousStack()?.let {
                mFragmentsStacks.remove(mCurrentStack)
                mCurrentStack = it
                stackChangeListener?.onStackChanged(it)
                return peekFragment() != null
            }
        }
        return false
    }

    private fun openFragment(fragment: Fragment, tag: String) {
        if(!mActivityRunning) {
            val transaction = object : DeferredFragmentTransaction(fragmentsContainer, fragment, tag) {
                override fun commit() {
                    openFragmentInternal(fragmentWrapperRes, deferredFragment, fragmentTag)
                }
            }
            mFragmentTransactions.add(transaction)
        } else {
            openFragmentInternal(fragmentsContainer, fragment, tag)
        }
    }

    private fun openFragmentInternal(container: Int, fragment: Fragment, tag: String) {
        mFragmentManager.beginTransaction().apply {
            if(fragment.isAdded) {
                show(fragment)
            } else {
                add(container, fragment, tag)
            }
            mFragmentManager.fragments.forEach {
                if(it.tag != tag) hide(it)
            }
            fragmentShowListener?.onFragmentShowed(fragment)
        }.commit()
    }

    private fun sortCurrentStacks(stackName: String) {
        val stack = mFragmentsStacks.remove(stackName)
        stack?.let {
            mFragmentsStacks.put(stackName, it)
        }
    }

    private fun getPreviousStack(): String? {
        val stackNames = mFragmentsStacks.keys.withIndex().filter { it.value == mCurrentStack }
        if(stackNames.isNotEmpty()) {
            val stackIndex = stackNames[0].index
            if (stackIndex - 1 >= 0 && !mFragmentsStacks.values.toList()[stackIndex - 1].isEmpty()) {
                return mFragmentsStacks.keys.toList()[stackIndex - 1]
            } else if(stackIndex > 0) {
                return getPreviousStack()
            }
        }
        return null
    }

    private fun pushTagToStack(tag: String) {
        val inx = mFragmentsStacks[mCurrentStack]?.withIndex()?.find { it.value == tag }?.index
        inx?.let {
            mFragmentsStacks[mCurrentStack]?.removeAt(it)
        }
        mFragmentsStacks[mCurrentStack]?.push(tag)
    }
}