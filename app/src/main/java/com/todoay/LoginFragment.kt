package com.todoay

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
import com.todoay.api.config.RetrofitService
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.api.domain.auth.login.LoginAPI
import com.todoay.databinding.FragmentLoginBinding
import com.todoay.global.util.TodoayApplication

class LoginFragment : Fragment() {

    private var mBinding : FragmentLoginBinding?= null

    //아이디 입력 여부 체크
    var isId : Boolean = false
    //비밀번호 입력 여부 체크
    var isPassword : Boolean = false

    private val loginService: LoginAPI = LoginAPI()
    private val emailService: EmailAPI = EmailAPI()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBinding.inflate(inflater,container,false)

        mBinding = binding

        // 이메일 로그인 기록이 있는 경우 저장된 이메일을 edittext에 세팅
        val userEmail = TodoayApplication.pref.getEmail()
        if(userEmail != "") {
            mBinding?.loginEmailEditText?.setText(userEmail)
        }

        // 아이디 edit text
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

        //로그인 button
        mBinding?.loginLoginBtn?.setOnClickListener {
            val inputEmail = mBinding?.loginEmailEditText!!.text.toString()
            val inputPassword = mBinding?.loginEtPassword!!.text.toString()

            val act = activity as MainActivity
            act.hideKeyboard(requireView())

            mBinding?.loginProgressBar?.visibility = View.VISIBLE

            // 이메일이 인증되었는지 확인하는 API
            emailService.checkEmailVerified(
                inputEmail,
                onResponse = {
                    Log.d("check email verified", "onResponse() called in LoginFragment")
                    // 이메일 인증을 완료한 경우
                    if(it.emailVerified) {
                        // 로그인 API
                        loginService.login(
                            inputEmail,
                            inputPassword,
                            onResponse = {
                                Log.d("login", "onResponse() called in LoginFragment")
                                Log.d("user-email", "$inputEmail")
                                TodoayApplication.pref.setUser(
                                    inputEmail,
                                    it.accessToken,
                                    it.refreshToken
                                )
                                if(TodoayApplication.pref.getAccessToken()!="") {
                                    mBinding?.loginProgressBar?.visibility = View.GONE
                                    RetrofitService.refresh() // Retrofit 객체 초기화
                                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_profileFragment)
                                }
                                else {
                                    Toast.makeText(requireContext(), "다시 로그인해주세요", Toast.LENGTH_LONG).show()
                                    mBinding?.loginProgressBar?.visibility = View.GONE
                                }
                            },
                            onErrorResponse = {
                                Log.d("login", "onErrorResponse() called in LoginFragment")
                                mBinding?.loginErrorMessage?.visibility = View.VISIBLE
                                mBinding?.loginEtPassword?.setText("")
                                mBinding?.loginProgressBar?.visibility = View.GONE
                            },
                            onFailure = {
                                Log.d("login", "onFailure() called in LoginFragment")
                                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                                mBinding?.loginProgressBar?.visibility = View.GONE
                            }
                        )
                    }
                    // 이메일 인증을 하지 않은 경우
                    else {
                        Log.d("check email verified", "Do not email verified in LoginFragment")
                        Toast.makeText(requireContext(), "이메일 인증을 완료해주세요", Toast.LENGTH_LONG).show()
                        mBinding?.loginProgressBar?.visibility = View.GONE
                    }
                },
                onErrorResponse = {
                    // Status 404 이메일에 해당하는 계정이 없음
                    Log.d("check email verified", "onErrorResponse() called in LoginFragment")
                    mBinding?.loginEmailNonExistsErrorMessage?.visibility = View.VISIBLE
                    mBinding?.loginEtPassword?.setText("")
                    mBinding?.loginProgressBar?.visibility = View.GONE
                },
                onFailure = {
                    Log.d("check email verified", "onFailure() called in LoginFragment")
                    Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                    mBinding?.loginProgressBar?.visibility = View.GONE
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

    /**
     * 자동 로그인
     */
    override fun onStart() {
        super.onStart()
        if(TodoayApplication.pref.getAccessToken()!="") {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_profileFragment)
        }
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