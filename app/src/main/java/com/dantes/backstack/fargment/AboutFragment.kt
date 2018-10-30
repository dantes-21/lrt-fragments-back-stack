package com.dantes.backstack.fargment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dantes.backstack.MainActivityRouter
import com.dantes.backstack.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    private lateinit var mRouter: MainActivityRouter

    companion object {
        fun newInstance() = AboutFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mRouter = context as MainActivityRouter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        more_btn.setOnClickListener {
            mRouter.openMoreFragment()
        }
    }
}