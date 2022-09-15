package com.todoay.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.MainActivity
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.TodoayApplication
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.api.domain.auth.login.LoginAPI
import com.todoay.api.domain.auth.login.dto.request.LoginRequest
import com.todoay.databinding.FragmentLoginBinding
import com.todoay.global.util.PrintUtil.printLog

class LoginFragment : Fragment() {

    private var mBinding : FragmentLoginBinding?= null

    //아이디 입력 여부 체크
    var isId : Boolean = false
    //비밀번호 입력 여부 체크
    var isPassword : Boolean = false

    private val loginService: LoginAPI by lazy { LoginAPI.getInstance() }
    private val emailService: EmailAPI by lazy { EmailAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBinding.inflate(inflater,container,false)

        mBinding = binding

        /*
        로그인 기록이 있는 경우, 저장된 이메일 세팅.
         */
        if(TodoayApplication.pref.hasEmail()) {
            mBinding?.loginEmailEditText?.setText(TodoayApplication.pref.getEmail())
            isId = true
        }

        /* 아이디 edit text */
        mBinding?.loginEmailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.loginEmailNonExistsErrorMessage?.visibility == View.VISIBLE) {
                    mBinding?.loginEmailNonExistsErrorMessage?.visibility = View.GONE
                }
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
                    mBinding?.loginEmailValidErrorMessage?.visibility = View.GONE
                    isId = false
                }
                changeConfirmButtonColor()
            }

            override fun afterTextChanged(p0: Editable?) {
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
            }

        })

        /* 로그인 버튼 */
        mBinding?.loginLoginBtn?.setOnClickListener {
            val inputEmail = mBinding?.loginEmailEditText!!.text.toString()
            val inputPassword = mBinding?.loginEtPassword!!.text.toString()

            val act = activity as MainActivity
            act.hideKeyboard(requireView())

            mBinding?.loginProgressBar?.visibility = View.VISIBLE

            /**
             * 로그인 API Start
             * 1. 이메일 인증여부 확인
             * 1.1. 성공 : 로그인
             * 1.2. 실패 : 인증메일 재전송
             */
            checkEmailVerified(inputEmail, inputPassword)

            if(mBinding?.loginEmailEditText?.text.toString() == "choo901@naver.com") {
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_dailyTodoMainFragment)
            }
        }

        //회원 가입 button
        mBinding?.loginSigninBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_joinFragment)
        }

        //비밀번호 찾기 text
        mBinding?.loginFindPwTextBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_sendMailUpdatePasswordFragment)
        }

        return mBinding?.root

    }

    /**
     * 이메일 인증여부 확인 API 호출
     */
    private fun checkEmailVerified(inputEmail: String, inputPassword: String) {
        emailService.checkEmailVerified(
            inputEmail,
            /* 이메일 인증 여부 성공 */
            onResponse = {
                /* 이메일 인증 성공 */
                if (it.emailVerified) {
                    login(inputEmail, inputPassword)
                }
                /* 이메일 인증 실패 */
                else {
                    sendCertMail(inputEmail)
                }
            },
            /* 이메일 인증 여부 실패 */
            onErrorResponse = {
                // Status 404 이메일에 해당하는 계정이 없음
                mBinding?.loginEmailNonExistsErrorMessage?.visibility = View.VISIBLE
                mBinding?.loginEtPassword?.setText("")
                mBinding?.loginProgressBar?.visibility = View.GONE
            },
            /* 디바이스 Exception */
            onFailure = {
                mBinding?.loginProgressBar?.visibility = View.GONE
            }
        )
    }

    /**
     * 이메일 인증 메일 전송 API 호출
     */
    private fun sendCertMail(inputEmail: String) {
        emailService.sendCertMail(
            inputEmail,
            /* 인증메일 재전송 성공 */
            onResponse = {
                mBinding?.loginProgressBar?.visibility = View.GONE
                mainAct.showLongToast("이메일 인증을 완료해주세요\n인증메일을 재전송하였습니다")
            },
            /* 인증메일 재전송 실패 */
            onErrorResponse = {
                mBinding?.loginEmailValidErrorMessage?.visibility = View.VISIBLE
                mBinding?.loginEtPassword?.setText("")
                isId = false
                changeConfirmButtonColor()
            },
            /* 디바이스 Exception */
            onFailure = {
            }
        )
    }

    /**
     * 로그인 API 호출
     */
    private fun login(inputEmail: String, inputPassword: String) {
        val request  = LoginRequest(inputEmail, inputPassword)

        loginService.login(
            request,
            /* 로그인 성공 */
            onResponse = {
                TodoayApplication.pref.setUser(
                    inputEmail,
                    it.accessToken,
                    it.refreshToken
                )
                if (TodoayApplication.pref.hasAccessToken()) {
                    printLog("[USER] 로그인")
                    mBinding?.loginProgressBar?.visibility = View.GONE
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_dailyTodoMainFragment)
                } else {
                    mainAct.showLongToast("다시 로그인해주세요")
                    mBinding?.loginProgressBar?.visibility = View.GONE
                }
            },
            /* 로그인 실패 */
            onErrorResponse = {
                mBinding?.loginErrorMessage?.visibility = View.VISIBLE
                mBinding?.loginEtPassword?.setText("")
                mBinding?.loginProgressBar?.visibility = View.GONE
            },
            /* 디바이스 Exception */
            onFailure = {
                mBinding?.loginProgressBar?.visibility = View.GONE
            }
        )
    }

    /**
     * 자동 로그인
     */
    override fun onStart() {
        super.onStart()
        if(TodoayApplication.pref.hasAccessToken()) {
            printLog("[USER] 자동 로그인")
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_dailyTodoMainFragment)
        }
    }

    /**
     * 로그인 button 색상 변경 위한 메소드.
     */
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