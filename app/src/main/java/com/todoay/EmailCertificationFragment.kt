package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentEmailCertificationBinding

class EmailCertificationFragment : Fragment() {

    private var mBinding : FragmentEmailCertificationBinding?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEmailCertificationBinding.inflate(inflater,container,false)

        mBinding = binding

        //이메일 입력 textfield
        mBinding?.emailCertEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (mBinding?.emailCertEmailEditText?.text.toString() != "") {
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

            }

        })

        mBinding?.emailCertVerifyCodeEditText?.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.emailCertVerifyCodeEditText?.text?.toString() != "") {
                    mBinding?.emailCertOkayBtn?.isEnabled = true
                    mBinding?.emailCertOkayBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
                }
                else{
                    mBinding?.emailCertOkayBtn?.isEnabled = false
                    mBinding?.emailCertOkayBtn?.setBackgroundResource(R.drawable.confirmbtn_fail_background)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        //중복확인 버튼
        mBinding?.emailCertSendVerifyCodeBtn?.setOnClickListener {
            mBinding?.emailCertSendVerifyCodeBtn?.visibility = View.VISIBLE
            mBinding?.emailCertVerifyCodeText?.visibility = View.VISIBLE
            mBinding?.emailCertVerifyCodeEditText?.visibility = View.VISIBLE
            mBinding?.emailCertResendBtn?.visibility = View.VISIBLE
        }

        //확인 버튼
        mBinding?.emailCertOkayBtn?.setOnClickListener {

            if(mBinding?.emailCertVerifyCodeEditText?.text.toString()=="1234") {
                Navigation.findNavController(requireView()).navigate(R.id.action_emailCertificationFragment_to_joinFragment)
            }
            else {
                mBinding?.emailCertErrorMessage?.visibility = View.VISIBLE
            }

        }
        //뒤로가기 버튼
        mBinding?.emailCertBackBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_emailCertificationFragment_to_loginFragment)
        }

        return mBinding?.root
    }


}