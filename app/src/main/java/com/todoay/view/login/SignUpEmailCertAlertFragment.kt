package com.todoay.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.databinding.FragmentSignUpEmailCertAlertBinding

class SignUpEmailCertAlertFragment : Fragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    private var mBinding: FragmentSignUpEmailCertAlertBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = FragmentSignUpEmailCertAlertBinding.inflate(inflater, container, false)

        mBinding = binding


        /**
         * 확인 버튼
         */
        mBinding?.signUpEmailCertAlertConfirmBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpEmailCertAlertFragment_to_loginFragment)
        }

        /**
         * 뒤로가기 버튼
         */
        mBinding?.signUpEmailCertAlertBackBtn?.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpEmailCertAlertFragment_to_loginFragment)
        }


        return mBinding?.root
    }

}