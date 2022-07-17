package com.todoay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todoay.databinding.FragmentEmailCertificationFindPasswordBinding

class EmailCertificationFindPasswordFragment : Fragment() {

    lateinit var emailCertificationFindPasswordFragmentBinding: FragmentEmailCertificationFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        emailCertificationFindPasswordFragmentBinding = FragmentEmailCertificationFindPasswordBinding.inflate(inflater)

        return emailCertificationFindPasswordFragmentBinding.root
    }

}