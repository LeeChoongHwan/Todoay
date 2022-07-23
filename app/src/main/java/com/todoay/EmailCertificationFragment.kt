package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.api.util.ValidErrorResponse
import com.todoay.databinding.FragmentEmailCertificationBinding
import java.util.regex.Pattern

class EmailCertificationFragment : Fragment() {

    private var mBinding : FragmentEmailCertificationBinding?= null

    val args : EmailCertificationFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEmailCertificationBinding.inflate(inflater,container,false)

        mBinding = binding

        //이메일 입력 textfield
        mBinding?.emailCertEmailEditText?.setText(args.inputEmail)

//        mBinding?.emailCertEmailEditText?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (mBinding?.emailCertEmailEditText?.text.toString() != "") {
//                    // 이메일 형식 패턴 매칭
//                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.emailCertEmailEditText?.text.toString()).matches()) {
//                        mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = true
//                        mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
//                        mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.main_color))
//                    }
//                    else {
//                        mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = false
//                        mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
//                        mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
//                    }
//                }
//                else {
//                    mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = false
//                    mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
//                    mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
//                    mBinding?.emailCertSendVerifyCodeBtn?.text = "인증메일 전송"
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                if (mBinding?.emailCertEmailEditText?.text.toString() != "") {
//                    // 이메일 형식 패턴 매칭
//                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.emailCertEmailEditText?.text.toString()).matches()) {
//                        mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = true
//                        mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
//                        mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.main_color))
//                    }
//                    else {
//                        mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = false
//                        mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
//                        mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
//                    }
//                }
//                else {
//                    mBinding?.emailCertSendVerifyCodeBtn?.isEnabled = false
//                    mBinding?.emailCertSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
//                    mBinding?.emailCertSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
//                    mBinding?.emailCertSendVerifyCodeBtn?.text = "인증메일 전송"
//                }
//            }
//
//        })

        //인증메일 전송 버튼
        mBinding?.emailCertSendVerifyCodeBtn?.setOnClickListener {
            mBinding?.emailCertAlertMsg?.visibility = View.VISIBLE
            mBinding?.emailCertSignUpBtn?.isEnabled = true
            mBinding?.emailCertSignUpBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
            mBinding?.emailCertSendVerifyCodeBtn?.text = "재전송"

            // 버튼 클릭 시 키보드 내리기
            val act = activity as MainActivity
            act.hideKeyboard(requireView())

            // 인증메일 전송 API 호출
            EmailAPI().sendCertMail(
                mBinding?.emailCertEmailEditText?.text.toString(),
                onResponse = {
                    mBinding?.emailCertAlertTv?.text = "메일을 전송하였습니다. 메일을 확인해주세요."
                    mBinding?.emailCertAlertTv?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.emailCertAlertTv?.visibility = View.VISIBLE
                    Log.d("email", "status: ${it.status}")
                },
                onFailure = {
                    if(it is ValidErrorResponse){
                        mBinding?.emailCertAlertTv?.text = "메일 전송에 실패하였습니다. 올바른 메일을 작성해주세요."
                        mBinding?.emailCertAlertTv?.setTextColor(resources.getColor(R.color.red))
                        mBinding?.emailCertAlertTv?.visibility = View.VISIBLE
                        Log.d("email", "${it.details}")
                    }
                }
            )
        }

        //가입하기 버튼
        mBinding?.emailCertSignUpBtn?.setOnClickListener {
            // 확인 버튼 클릭 시 인증이 완료되었는지 확인하는 API 호출하여 success하면 다음 페이지로 이동


//            Navigation.findNavController(requireView()).navigate(R.id.action_emailCertificationFragment_to_loginFragment)
        }
        //뒤로가기 버튼
        mBinding?.emailCertBackBtn?.setOnClickListener {
//            Navigation.findNavController(requireView()).navigate(R.id.action_emailCertificationFragment_to_joinFragment)
        }

        return mBinding?.root
    }



}