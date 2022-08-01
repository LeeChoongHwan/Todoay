package com.todoay.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.MainActivity
import com.todoay.R
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.databinding.FragmentSendMailUpdatePasswordBinding

class SendMailUpdatePasswordFragment : Fragment() {

    private var mBinding : FragmentSendMailUpdatePasswordBinding?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSendMailUpdatePasswordBinding.inflate(inflater,container,false)

        mBinding = binding

        //뒤로가기 버튼
        mBinding?.sendMailUpdatePasswordBackBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_sendMailUpdatePasswordFragment_to_loginFragment)
        }

        //이메일 입력 textfield
        mBinding?.sendMailUpdatePasswordEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.sendMailUpdatePasswordEmailEditText?.text.toString() != "") {
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.sendMailUpdatePasswordEmailEditText?.text.toString()).matches()) {
                        mBinding?.sendMailUpdatePasswordSendMailBtn?.isEnabled = true
                        mBinding?.sendMailUpdatePasswordSendMailBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                        mBinding?.sendMailUpdatePasswordSendMailBtn?.setTextColor(resources.getColor(R.color.main_color))
                        mBinding?.sendMailUpdatePasswordEmailValidErrorMessage?.visibility = View.GONE
                    }
                    else{
                        mBinding?.sendMailUpdatePasswordSendMailBtn?.isEnabled = false
                        mBinding?.sendMailUpdatePasswordSendMailBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                        mBinding?.sendMailUpdatePasswordSendMailBtn?.setTextColor(resources.getColor(R.color.gray))
                        mBinding?.sendMailUpdatePasswordEmailValidErrorMessage?.visibility = View.VISIBLE
                    }
                }
                else {
                    mBinding?.sendMailUpdatePasswordSendMailBtn?.isEnabled = false
                    mBinding?.sendMailUpdatePasswordSendMailBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.sendMailUpdatePasswordSendMailBtn?.setTextColor(resources.getColor(R.color.gray))
                    mBinding?.sendMailUpdatePasswordEmailValidErrorMessage?.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        // 메일 전송 버튼
        mBinding?.sendMailUpdatePasswordSendMailBtn?.setOnClickListener {
            val inputEmail = mBinding?.sendMailUpdatePasswordEmailEditText?.text.toString()
            mBinding?.sendMailUpdatePasswordSendMailBtn?.text = "재전송"

            // 버튼 클릭 시 키보드 내리기
            val act = activity as MainActivity
            act.hideKeyboard(requireView())

            // 임시비밀번호 메일 전송 API 호출
            EmailAPI().sendMailForUpdatePassword(
                inputEmail,
                onResponse = {
                    Log.d("send mail for update password", "onResponse() called in SendMailUpdatePasswordFragment")
                    mBinding?.sendMailUpdatePasswordSendMailAlertMessage?.text = "메일이 전송되었습니다"
                    mBinding?.sendMailUpdatePasswordSendMailAlertMessage?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.sendMailUpdatePasswordSendMailAlertMessage?.visibility = View.VISIBLE

                    mBinding?.sendMailUpdatePasswordConfirmBtn?.isEnabled = true
                    mBinding?.sendMailUpdatePasswordConfirmBtn?.setBackgroundResource(R.drawable.confirmbtn_background)

                    mBinding?.sendMailUpdatePasswordConfirmMessage?.visibility = View.VISIBLE
                },
                onErrorResponse = {
                    if(it is ValidErrorResponse){
                        Log.d("send mail for update password", "onErrorResponse() called in SendMailUpdatePasswordFragment")
                        mBinding?.sendMailUpdatePasswordSendMailAlertMessage?.text = "메일 전송이 실패하였습니다"
                        mBinding?.sendMailUpdatePasswordSendMailAlertMessage?.setTextColor(resources.getColor(R.color.red))
                        mBinding?.sendMailUpdatePasswordSendMailAlertMessage?.visibility = View.VISIBLE
                    }
                },
                onFailure = {
                    Log.d("send mail for update password", "onFailure() called in SendMailUpdatePasswordFragment")
                    Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                }
            )
        }

        //확인 버튼
        mBinding?.sendMailUpdatePasswordConfirmBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_sendMailUpdatePasswordFragment_to_loginFragment)
        }


        return mBinding?.root
    }

}