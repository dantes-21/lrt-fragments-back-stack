package com.dantes.backstack

interface MainActivityRouter {

    fun openIdeasFragment()

    fun openTimeFragment()

    fun openFavoritesFragment()

    fun openAboutFragment()

    fun openMoreFragment()

    fun openListFragment()

    fun openItemFragment(itemText: String)

}