package com.dantes.backstack

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.dantes.backstack.fargment.*
import com.dantes.lrtbackstack.StackAppCompatActivity
import com.dantes.lrtbackstack.manager.FragmentStackManager
import com.dantes.lrtbackstack.manager.FragmentStackManagerBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : StackAppCompatActivity(),
        MainActivityRouter,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private val mFragmentManager = FragmentStackManagerBuilder(this)
            .fragmentsContainer(R.id.fragment_wrapper)
            .stackChangeListener(this)
            .useStacksHistory(true)
            .build()

    companion object {
        private const val IDEAS     = "ideas"
        private const val TIME      = "time"
        private const val FAVORITES = "favorites"
        private const val ABOUT     = "about"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if(savedInstanceState == null) {
            openIdeasFragment()
        }
    }

    override fun onNavigationItemSelected(p: MenuItem): Boolean {
        when(p.itemId) {
            R.id.ideas     -> openIdeasFragment()
            R.id.time      -> openTimeFragment()
            R.id.favorites -> openFavoritesFragment()
            R.id.about     -> openAboutFragment()
            else -> return false
        }
        return true
    }

    override fun onStackChanged(stackName: String) {
        when(stackName) {
            IDEAS     -> bottom_navigation.menu.findItem(R.id.ideas).isChecked = true
            TIME      -> bottom_navigation.menu.findItem(R.id.time).isChecked = true
            FAVORITES -> bottom_navigation.menu.findItem(R.id.favorites).isChecked = true
            ABOUT     -> bottom_navigation.menu.findItem(R.id.about).isChecked = true
        }
    }

    override fun onBackPressed() {
        if(!mFragmentManager.onBackPressed()) {
            this.finish()
        }
    }

    override fun openIdeasFragment() {
        mFragmentManager.changeStack(IDEAS)
        if(mFragmentManager.peekFragment() == null) {
            mFragmentManager.pushFragment(IdeasFragment.newInstance(), IdeasFragment::class.java.name)
        }
    }

    override fun openTimeFragment() {
        mFragmentManager.changeStack(TIME)
        if(mFragmentManager.peekFragment() == null) {
            mFragmentManager.pushFragment(TimeFragment.newInstance(), TimeFragment::class.java.name)
        }
    }

    override fun openFavoritesFragment() {
        mFragmentManager.changeStack(FAVORITES)
        if(mFragmentManager.peekFragment() == null) {
            mFragmentManager.pushFragment(FavoritesFragment.newInstance(), FavoritesFragment::class.java.name)
        }
    }

    override fun openAboutFragment() {
        mFragmentManager.changeStack(ABOUT)
        if(mFragmentManager.peekFragment() == null) {
            mFragmentManager.pushFragment(AboutFragment.newInstance(), AboutFragment::class.java.name)
        }
    }

    override fun openMoreFragment() {
        mFragmentManager.pushFragment(MoreFragment.newInstance(), MoreFragment::class.java.name)
    }

    override fun openListFragment() {
        mFragmentManager.pushFragment(ListFragment.newInstance(), ListFragment::class.java.name)
    }

    override fun openItemFragment(itemText: String) {
        mFragmentManager.pushFragment(ItemFragment.newInstance(itemText), ItemFragment::class.java.name)
    }

    override fun getFragmentStackManager(): FragmentStackManager {
        return mFragmentManager
    }
}
