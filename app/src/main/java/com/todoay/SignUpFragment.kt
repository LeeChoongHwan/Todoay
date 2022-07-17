package com.todoay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.todoay.databinding.FragmentEmailCertificationBinding
import com.todoay.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    lateinit var singUpFragmentBinding : FragmentSignUpBinding
    lateinit var emailCertificationFragmentBinding: FragmentEmailCertificationBinding
    lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        singUpFragmentBinding = FragmentSignUpBinding.inflate(inflater)

        return singUpFragmentBinding.root
    }

}