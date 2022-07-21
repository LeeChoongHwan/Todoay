package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        //이메일 입력 textfield
        mBinding?.emailCertEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.emailCertEmailEditText?.text.toString() != "") {
                    mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = true
                    mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.main_color))
                }
                else {
                    mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = false
                    mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.emailCertEmailEditText?.text.toString() != "") {
                    mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = true
                    mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.main_color))
                }
                else {
                    mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = false
                    mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
                }
            }

        })

        //뒤로가기 버튼
        mBinding?.emailCertBackBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_emailCertificationFindPasswordFragment_to_loginFragment)
        }

        //확인 버튼
        mBinding?.emailCertOkayBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_emailCertificationFindPasswordFragment_to_loginFragment)
        }

        //인증메일 전송 버튼
        mBinding?.emailCertSendVerifyCodeBtn?.setOnClickListener {
            mBinding?.emailCertErrormessage?.visibility = View.VISIBLE
            mBinding?.emailCertOkayBtn?.isEnabled = true
            mBinding?.emailCertOkayBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
        }

        return mBinding?.root
    }

}