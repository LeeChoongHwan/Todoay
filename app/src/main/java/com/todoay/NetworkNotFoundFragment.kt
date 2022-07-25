package com.todoay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todoay.databinding.FragmentNetworkNotFoundBinding

class NetworkNotFoundFragment : Fragment() {

    private var mBinding : FragmentNetworkNotFoundBinding?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNetworkNotFoundBinding.inflate(inflater, container, false)

        mBinding = binding




        return mBinding?.root
    }

}