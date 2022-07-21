package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentMyinfoBinding
import com.todoay.databinding.FragmentMyinfoModifyBinding

class MyinfoModifyFragment : Fragment() {

    private var mBinding : FragmentMyinfoModifyBinding?= null

    var isNickname : Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyinfoModifyBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.myinfomodifyNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.myinfomodifyNicknameEt?.text.toString() != "") {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = true
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.main_color)
                }
                else {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = false
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.gray)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.myinfomodifyNicknameEt?.text.toString() != "") {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = true
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.main_color)
                }
                else {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = false
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.gray)
                }
            }

        })

        mBinding?.myinfomodifyNicknamecheckBtn?.setOnClickListener {
            mBinding?.myinfoNicknameErrormessage?.visibility = View.VISIBLE
            isNickname = true
            changeConfirmButton()
        }

        mBinding?.myinfomodifyBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
        }

        mBinding?.myinfoConfirmBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
        }

        return mBinding?.root
    }

    private fun changeConfirmButton() {
        if(isNickname == true) {
            mBinding?.myinfoConfirmBtn?.setTextColor(R.color.main_color)
            mBinding?.myinfoConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.myinfoConfirmBtn?.setTextColor(R.color.gray)
            mBinding?.myinfoConfirmBtn?.isEnabled = false
        }
    }
}