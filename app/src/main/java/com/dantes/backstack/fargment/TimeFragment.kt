package com.dantes.backstack.fargment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dantes.backstack.R
import kotlinx.android.synthetic.main.fragment_time.*
import java.text.SimpleDateFormat
import java.util.*

class TimeFragment : Fragment() {

    private val format = SimpleDateFormat("hh:mm:ss MMM dd, yyyy", Locale.US)
    private val handler = Handler()
    private val runnable = object : Runnable {
        override fun run() {
            time.text = format.format(System.currentTimeMillis())
            handler.postDelayed(this, 1000L)
        }
    }

    companion object {
        fun newInstance() = TimeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runnable.run()
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 1000L)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }
}