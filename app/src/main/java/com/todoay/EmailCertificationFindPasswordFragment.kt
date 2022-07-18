package com.todoay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentChangePasswordBinding
import com.todoay.databinding.FragmentEmailCertificationFindPasswordBinding

class EmailCertificationFindPasswordFragment : Fragment() {

    private var mBinding : FragmentEmailCertificationFindPasswordBinding?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEmailCertificationFindPasswordBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.emailCertBackBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_emailCertificationFindPasswordFragment_to_loginFragment)
        }

        mBinding?.emailCertOkayBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_emailCertificationFindPasswordFragment_to_loginFragment)
        }

        return mBinding?.root
    }

}