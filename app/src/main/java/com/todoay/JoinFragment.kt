package com.todoay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentEmailCertificationFindPasswordBinding
import com.todoay.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {

    private var mBinding : FragmentJoinBinding?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentJoinBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.joinBackBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_joinFragment_to_emailCertificationFragment2)
        }

        mBinding?.joinConfirmBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_joinFragment_to_loginFragment)
        }

        return mBinding?.root
    }
}