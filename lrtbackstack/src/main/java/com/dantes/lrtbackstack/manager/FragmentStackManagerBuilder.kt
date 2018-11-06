package com.dantes.lrtbackstack.manager

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.dantes.lrtbackstack.exceptions.WrongFragmentContainerException
import com.dantes.lrtbackstack.listeners.FragmentShowListener
import com.dantes.lrtbackstack.listeners.StackChangeListener

/**
 * Simple Fragment Stack Manager builder to create the Fragments Stack Manager with all
 * necessary parameters
 */
class FragmentStackManagerBuilder(activity: AppCompatActivity) {
    private val mFragmentManager = FragmentStackManagerImpl(activity)

    /**
     * Set Change Stack Manager to handle changing of the current stack. It may be useful
     * when you use a bottom navigation and have to change the selected item when
     * stack is changed
     *
     * @param listener - the Stack Change listener
     */
    fun stackChangeListener(listener: StackChangeListener): FragmentStackManagerBuilder {
        mFragmentManager.stackChangeListener = listener
        return this
    }

    /**
     * Set Fragment Change listener that will be triggered after fragment will be showed.
     * It may be useful when the user will go back using the Back button
     *
     * @param listener - the Fragment Show Listener
     */
    fun fragmentShowListener(listener: FragmentShowListener): FragmentStackManagerBuilder {
        mFragmentManager.fragmentShowListener = listener
        return this
    }

    /**
     * If this is true, then when the end of the current stack is reached, the previous
     * stack will be selected as the current stack. When param is false and the end
     * of the stack is reached, the onBackPressed or some other functions will return false
     *
     * @param useStacksHistory - tru if history will be used, false otherwise
     */
    fun useStacksHistory(useStacksHistory: Boolean): FragmentStackManagerBuilder {
        mFragmentManager.useStacksHistory = useStacksHistory
        return this
    }

    /**
     * Sets the Fragment Container layout ID
     *
     * @param wrapper - layout ID
     */
    fun fragmentsContainer(@IdRes wrapper: Int): FragmentStackManagerBuilder {
        mFragmentManager.fragmentsContainer = wrapper
        return this
    }

    /**
     * Return a created Fragment Stack Manager with selected parameters
     *
     * @return the Fragment Stack Manager object
     * @throws WrongFragmentContainerException when fragment container not set or equal to 0
     */
    fun build(): FragmentStackManager {
        if(mFragmentManager.fragmentsContainer == 0)
            throw WrongFragmentContainerException()
        else return mFragmentManager
    }
}