package com.todoay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todoay.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var loginFragmentBinding : FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loginFragmentBinding = FragmentLoginBinding.inflate(inflater)

        loginFragmentBinding.loginSignUpTextBtn.setOnClickListener {
            val act = activity as MainActivity
            act.fragmentController(FragmentControllerName.CONTROLLER_SIGN_UP, true)
        }

        return loginFragmentBinding.root
    }

}