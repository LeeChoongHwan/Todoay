package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.todoay.api.domain.auth.login.LoginAPI
import com.todoay.api.domain.auth.login.LoginService
import com.todoay.databinding.FragmentLoginBinding
import com.todoay.global.util.TodoayApplication
import java.net.ConnectException

class LoginFragment : Fragment() {

    private var mBinding : FragmentLoginBinding?= null

    //아이디 입력 여부 체크
    var isId : Boolean = false
    //비밀번호 입력 여부 체크
    var isPassword : Boolean = false

    private val loginService: LoginAPI = LoginAPI()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBinding.inflate(inflater,container,false)

        mBinding = binding

        //아이디 edit text
        mBinding?.loginEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.loginEmailEditText!!.text.toString() != "") {
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.loginEmailEditText?.text.toString()).matches()) {
                        mBinding?.loginEmailValidErrorMessage?.visibility = View.GONE
                        isId = true
                    }
                    else {
                        mBinding?.loginEmailValidErrorMessage?.visibility = View.VISIBLE
                        isId = false
                    }
                }
                else {
                    isId = false
                }
                changeConfirmButtonColor()
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.loginEmailEditText!!.text.toString() != "") {
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.loginEmailEditText?.text.toString()).matches()) {
                        mBinding?.loginEmailValidErrorMessage?.visibility = View.GONE
                        isId = true
                    }
                    else {
                        mBinding?.loginEmailValidErrorMessage?.visibility = View.VISIBLE
                        isId = false
                    }
                }
                else {
                    isId = false
                }
                changeConfirmButtonColor()
            }

        })
        //비밀번호 edit text
        mBinding?.loginEtPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isPassword = mBinding?.loginEtPassword!!.text.toString() != ""
                changeConfirmButtonColor()
            }

            override fun afterTextChanged(p0: Editable?) {
                isPassword = mBinding?.loginEtPassword!!.text.toString() != ""
                changeConfirmButtonColor()
            }

        })

        //로그인 button
        mBinding?.loginLoginBtn?.setOnClickListener {
            // 이메일이 인증되었는지 확인하는 API

            // 로그인 API
            loginService.login(
                mBinding?.loginEmailEditText!!.text.toString(),
                mBinding?.loginEtPassword!!.text.toString(),
                onResponse = {
                    Log.d("login", "onResponse() called in LoginFragment")
                    TodoayApplication.pref.setAccessToken(it.accessToken)
                    TodoayApplication.pref.setRefreshToken(it.refreshToken)
                    Log.d("login", "AccessToken is ${it.accessToken}")
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_myinfoFragment)
                },
                onErrorResponse = {
                    Log.d("login", "onErrorResponse() called in LoginFragment")
                    mBinding?.loginErrorMessage?.visibility = View.VISIBLE
                    mBinding?.loginEtPassword?.setText("")
                    mBinding?.loginEmailEditText?.requestFocus()
                },
                onFailure = {
                    Log.d("login", "onFailure() called in LoginFragment")
                    Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                }
            )

        }

        //회원 가입 button
        mBinding?.loginSigninBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_joinFragment)
        }

        //비밀번호 찾기 text
        mBinding?.loginFindPwTextBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_emailCertForgetPasswordFragment)
        }


        return mBinding?.root

    }
    //로그인 button 색상 변경 위한 함수
    private fun changeConfirmButtonColor() {
        if(isId && isPassword) {
            mBinding?.loginLoginBtn?.isEnabled = true
            mBinding?.loginLoginBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
        }
        else {
            mBinding?.loginLoginBtn?.isEnabled = false
            mBinding?.loginLoginBtn?.setBackgroundResource(R.drawable.confirmbtn_fail_background)
        }
    }

}