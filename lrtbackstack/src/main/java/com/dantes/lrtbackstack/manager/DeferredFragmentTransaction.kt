package com.dantes.lrtbackstack.manager

import android.support.v4.app.Fragment

/**
 * The fragment may sometimes be committed after the activity's onStop method. This situation
 * produces an IllegalStateException and causes further crashes. To avoid this, we will use
 * deferred transactions. The transaction information must be saved using this class.
 */
abstract class DeferredFragmentTransaction(var fragmentWrapperRes: Int,
                                           var deferredFragment: Fragment,
                                           var fragmentTag: String) {

    abstract fun commit()

}