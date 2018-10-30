package com.dantes.backstack.fargment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dantes.backstack.MainActivityRouter
import com.dantes.backstack.R
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private lateinit var mRouter: MainActivityRouter

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mRouter = context as MainActivityRouter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mutableListOf<String>()
        for(i in 0 until 200) {
            list.add("Item number = $i")
        }

        dynamic.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.item_list, list)
        dynamic.setOnItemClickListener { _, _, position, _ ->
            mRouter.openItemFragment(list.get(position))
        }
    }
}