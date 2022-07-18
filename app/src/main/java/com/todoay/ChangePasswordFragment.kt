package com.todoay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentChangePasswordBinding
import com.todoay.databinding.FragmentEmailCertificationBinding

class ChangePasswordFragment : Fragment() {

    private var mBinding : FragmentChangePasswordBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChangePasswordBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.changepasswordBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_changePasswordFragment_to_myinfoFragment)
        }

        return mBinding?.root
    }
}