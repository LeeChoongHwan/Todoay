package com.todoay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todoay.databinding.FragmentFindPasswordBinding

class FindPasswordFragment : Fragment() {

    lateinit var findPasswordFragmentBinding : FragmentFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        findPasswordFragmentBinding = FragmentFindPasswordBinding.inflate(inflater)

        return findPasswordFragmentBinding.root
    }

}