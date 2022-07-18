package com.todoay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentMyinfoBinding
import com.todoay.databinding.FragmentMyinfoModifyBinding

class MyinfoModifyFragment : Fragment() {

    private var mBinding : FragmentMyinfoModifyBinding?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyinfoModifyBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.myinfomodifyBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
        }

        mBinding?.myinfomodifyBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
        }

        return mBinding?.root
    }
}