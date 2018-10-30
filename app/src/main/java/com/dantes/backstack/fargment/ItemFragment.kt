package com.dantes.backstack.fargment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dantes.backstack.R
import kotlinx.android.synthetic.main.fragment_item.*

class ItemFragment : Fragment() {

    companion object {
        private const val ITEM = "lrt.back.stack.item"

        fun newInstance(text: String): ItemFragment {
            val fragment = ItemFragment()

            val b = Bundle()
            b.putString(ITEM, text)
            fragment.arguments = b

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            item_text.text = it.getString(ITEM)
        }
    }
}