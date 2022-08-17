package com.todoay.view.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.todoay.databinding.FragmentNetworkNotFoundBinding
import com.todoay.global.util.Utils.Companion.printLogView

class NetworkNotFoundFragment : Fragment() {

    private var mBinding : FragmentNetworkNotFoundBinding?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNetworkNotFoundBinding.inflate(inflater, container, false)

        mBinding = binding

        printLogView(this)

        return mBinding?.root
    }

}