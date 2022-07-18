package com.todoay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentLoginBinding
import com.todoay.databinding.FragmentMyinfoBinding

class MyinfoFragment : Fragment() {

    private var mBinding : FragmentMyinfoBinding?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyinfoBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.myinfoModifyinfoBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoFragment_to_myinfoModifyFragment)
        }

        mBinding?.myinfoChangepasswordTv?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoFragment_to_changePasswordFragment)
        }

        return mBinding?.root
    }
}