package com.todoay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todoay.databinding.ActivityMainBinding
import com.todoay.databinding.FragmentEmailCertificationBinding

class EmailCertificationFragment : Fragment() {

    lateinit var emailCertificationFragmentBinding: FragmentEmailCertificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        emailCertificationFragmentBinding = FragmentEmailCertificationBinding.inflate(inflater)

        return emailCertificationFragmentBinding.root
    }


}