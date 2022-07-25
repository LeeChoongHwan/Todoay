package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.api.util.ValidErrorResponse
import com.todoay.databinding.FragmentEmailCertForgetPasswordBinding

class EmailCertForgetPasswordFragment : Fragment() {

    private var mBinding : FragmentEmailCertForgetPasswordBinding?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEmailCertForgetPasswordBinding.inflate(inflater,container,false)

        mBinding = binding

        //이메일 입력 textfield
        mBinding?.emailCertForgetPasswordEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.emailCertForgetPasswordEmailEditText?.text.toString() != "") {
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.emailCertForgetPasswordEmailEditText?.text.toString()).matches()) {
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.isEnabled = true
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.main_color))
                        mBinding?.emailCertForgetPasswordEmailValidErrorMessage?.visibility = View.GONE
                    }
                    else{
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.isEnabled = false
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
                        mBinding?.emailCertForgetPasswordEmailValidErrorMessage?.visibility = View.VISIBLE
                    }
                }
                else {
                    mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.isEnabled = false
                    mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
                    mBinding?.emailCertForgetPasswordEmailValidErrorMessage?.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.emailCertForgetPasswordEmailEditText?.text.toString() != "") {
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.emailCertForgetPasswordEmailEditText?.text.toString()).matches()) {
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.isEnabled = true
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.main_color))
                        mBinding?.emailCertForgetPasswordEmailValidErrorMessage?.visibility = View.GONE
                    }
                    else{
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.isEnabled = false
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
                        mBinding?.emailCertForgetPasswordEmailValidErrorMessage?.visibility = View.VISIBLE
                    }
                }
                else {
                    mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.isEnabled = false
                    mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setTextColor(resources.getColor(R.color.gray))
                    mBinding?.emailCertForgetPasswordEmailValidErrorMessage?.visibility = View.VISIBLE
                }
            }

        })

        //인증메일 전송 버튼
        mBinding?.emailCertForgetPasswordSendVerifyCodeBtn?.setOnClickListener {
            mBinding?.emailCertForgetPasswordErrorMessage?.visibility = View.VISIBLE
            mBinding?.emailCertForgetPasswordOkayBtn?.isEnabled = true
            mBinding?.emailCertForgetPasswordOkayBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
            mBinding?.emailCertForgetPasswordOkayBtn?.text = "재전송"

            // 버튼 클릭 시 키보드 내리기
            val act = activity as MainActivity
            act.hideKeyboard(requireView())

            // 인증메일 전송 API 호출
            EmailAPI().sendCertMail(
                mBinding?.emailCertForgetPasswordEmailEditText?.text.toString(),
                onResponse = {
                    mBinding?.emailCertForgetPasswordSendMailAlertMessage?.text = "메일을 전송하였습니다. 메일을 확인해주세요."
                    mBinding?.emailCertForgetPasswordSendMailAlertMessage?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.emailCertForgetPasswordSendMailAlertMessage?.visibility = View.VISIBLE
                    Log.d("email", "status: $it")
                },
                onErrorResponse = {
                    if(it is ValidErrorResponse){
                        mBinding?.emailCertForgetPasswordSendMailAlertMessage?.text = "메일 전송에 실패하였습니다. 올바른 메일을 작성해주세요."
                        mBinding?.emailCertForgetPasswordSendMailAlertMessage?.setTextColor(resources.getColor(R.color.red))
                        mBinding?.emailCertForgetPasswordSendMailAlertMessage?.visibility = View.VISIBLE
                        Log.d("email", "$it")
                    }
                },
                onFailure = {

                }
            )
        }

        //뒤로가기 버튼
        mBinding?.emailCertForgetPasswordBackBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_emailCertForgetPasswordFragment_to_loginFragment)
        }

        //확인 버튼
        mBinding?.emailCertForgetPasswordOkayBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_emailCertForgetPasswordFragment_to_loginFragment)
        }


        return mBinding?.root
    }

}