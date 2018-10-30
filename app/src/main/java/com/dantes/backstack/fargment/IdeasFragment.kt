package com.dantes.backstack.fargment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dantes.backstack.MainActivityRouter
import com.dantes.backstack.R

class IdeasFragment : Fragment() {

    private lateinit var mRouter: MainActivityRouter

    companion object {
        fun newInstance() = IdeasFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mRouter = context as MainActivityRouter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ideas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button2).setOnClickListener {
            mRouter.openListFragment()
        }
    }
}