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
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.TodoayApplication
import com.todoay.api.domain.auth.AuthAPI
import com.todoay.api.domain.auth.dto.request.SignUpRequest
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.databinding.FragmentSignUpBinding
import java.net.HttpURLConnection
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    private var mBinding : FragmentSignUpBinding?= null

    var isEmail : Boolean = false
    var isPassword : Boolean = false
    var isPasswordCheck : Boolean = false
    var isNickname : Boolean = false
    var isPasswordMatchCondition : Boolean = false

    private val authService : AuthAPI by lazy { AuthAPI.getInstance() }
    private val emailService : EmailAPI by lazy { EmailAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSignUpBinding.inflate(inflater,container,false)

        mBinding = binding

        /* 토큰 초기화 */
        TodoayApplication.pref.clear()

        /* 이메일 필드 입력 */
        mBinding?.signUpEmailEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 이메일 editText가 공백이 아닐 경우
                if(mBinding?.signUpEmailEt?.text.toString() != "") {
                    // 이메일 형식 패턴 매칭
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.signUpEmailEt?.text.toString()).matches()) {
                        mBinding?.signUpEmailValidTv?.visibility = View.GONE
                        isEmail = true
                    }
                    else {
                        mBinding?.signUpEmailValidTv?.visibility = View.VISIBLE
                        isEmail = false
                    }
                    //
                    if(mBinding?.signUpEmailCheckPossibleMessageTv?.visibility == View.VISIBLE || mBinding?.signUpEmailCheckErrorMessageTv?.visibility == View.VISIBLE){
                        mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.GONE
                        mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.GONE
                    }
                }
                // 이메일 editText가 공백일 경우
                else {
                    mBinding?.signUpEmailValidTv?.visibility = View.GONE
                    mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.GONE
                    mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.GONE
                    isEmail = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 비밀번호 필드 입력 */
        mBinding?.signUpPasswordEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.signUpPasswordEt?.text.toString() != "") {
                    checkPasswordMatchCondition()
                    isPassword = true
                }
                else {
                    checkPasswordMatchCondition()
                    isPassword = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 비밀번호 확인 필드 입력 */
        mBinding?.signUpPasswordCheckEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.signUpPasswordEt?.text.toString() == mBinding?.signUpPasswordCheckEt?.text.toString()) {
                    mBinding?.signUpPasswordCheckErrorMessage?.visibility = View.GONE
                    isPasswordCheck = true
                }
                else {
                    mBinding?.signUpPasswordCheckErrorMessage?.visibility = View.VISIBLE
                    isPasswordCheck = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 닉네임 필드 입력 */
        mBinding?.signUpNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputNickname = mBinding?.signUpNicknameEt?.text.toString()
                if(inputNickname != "") {
                    if(!inputNickname.contains(" ") && Pattern.matches("^[a-zA-Z0-9_]*\$", inputNickname)) {
                        checkNicknameExists(inputNickname)
                    }
                    else {
                        mBinding?.signUpNicknameAlertMsgTv?.text = "공백 또는 특수문자('_'제외)를 입력할 수 없습니다"
                        mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                        mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                        isNickname = false
                        changeConfirmButton()
                    }
                }
                else {
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.GONE
                    isNickname = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 뒤로가기 버튼 */
        mBinding?.signUpBackBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        /*
         * 가입하기 버튼
         * 1. 이메일 중복확인 API 호출
         * 2. 회원가입 API 호출
         * 3. 이메일 인증메일 전송 API 호출
         */
        mBinding?.signUpSignUpBtn?.setOnClickListener {
            val inputEmail = mBinding?.signUpEmailEt?.text.toString()
            val inputPassword = mBinding?.signUpPasswordEt?.text.toString()
            val inputNickname = mBinding?.signUpNicknameEt?.text.toString()

            /**
             * 회원가입 API Start
             */
            signUpLogicForAPI(inputEmail, inputPassword, inputNickname)
        }

        return mBinding?.root
    }

    /**
     * 닉네임 중복확인 API 호출 메소드
     */
    private fun checkNicknameExists(inputNickname: String) {
        authService.checkNicknameExists(
            inputNickname,
            onResponse = {
                if (!it.nicknameExist) {
                    mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 있는 닉네임입니다"
                    mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isNickname = true
                    changeConfirmButton()
                }
                else {
                    mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                    mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isNickname = false
                    changeConfirmButton()
                }
            },
            onErrorResponse = {
                /* 400 유효성 검사 실패 */
                mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                isNickname = false
                changeConfirmButton()
            },
            onFailure = {}
        )
    }

    /**
     * 회원가입 API 로직 메소드
     */
    private fun signUpLogicForAPI(inputEmail: String, inputPassword: String, inputNickname: String) {
        checkEmailExists(inputEmail, inputPassword, inputNickname)
    }

    /**
     * 이메일 중복확인 API 호출 메소드
     */
    private fun checkEmailExists(inputEmail: String, inputPassword: String, inputNickname: String) {
        emailService.checkEmailExists(
            inputEmail,
            onResponse = {
                /* 이메일이 중복하지 않을 경우 */
                if (!it.emailExists) {
                    signUp(inputEmail, inputPassword, inputNickname)
                    mBinding?.signUpProgressBar?.visibility = View.VISIBLE
                }
                /* 이메일이 중복할 경우 */
                else {
                    mBinding?.signUpEmailEt?.requestFocus()
                    mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.VISIBLE
                    mBinding?.signUpProgressBar?.visibility = View.GONE
                }
            },
            onErrorResponse = {
                /* 400 이메일 패턴 유효성 검사 실패 */
                mBinding?.signUpEmailEt?.requestFocus()
                mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.VISIBLE
                mBinding?.signUpProgressBar?.visibility = View.GONE
            },
            onFailure = {
                mBinding?.signUpProgressBar?.visibility = View.GONE
            }
        )
    }

    /**
     * 회원가입 유저 리소스 생성 요청 API 호출 메소드
     */
    private fun signUp(inputEmail: String, inputPassword: String, inputNickname: String) {
        val request = SignUpRequest(inputEmail, inputPassword, inputNickname)

        authService.signUp(
            request,
            onResponse = {
                if (it.status == HttpURLConnection.HTTP_NO_CONTENT) {
                    Navigation.findNavController(requireView()).navigate(R.id.action_joinFragment_to_signUpEmailCertAlertFragment)
                    sendCertMail(inputEmail)
                }
            },
            onErrorResponse = {
                // Status == 400 유효성 검사 실패
                // Status == 403 이메일 혹은 닉네임 중복
                it.details.stream().map { validDetail ->
                    when(validDetail.field) {
                        "email" -> {
                            mBinding?.signUpEmailValidTv?.visibility = View.VISIBLE
                            mBinding?.signUpEmailEt?.requestFocus()
                            isEmail = false
                        }
                        "password" -> {
                            mBinding?.signUpPasswordErrorMessage?.visibility = View.VISIBLE
                            mBinding?.signUpPasswordEt?.setText("")
                            mBinding?.signUpPasswordCheckEt?.setText("")
                            mBinding?.signUpPasswordEt?.requestFocus()
                            isPassword = false
                        }
                        "nickname" -> {
                            mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                            mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                            mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                            mBinding?.signUpNicknameEt?.requestFocus()
                            isNickname = false
                        }
                        else -> {
                            mainAct!!.showLongToast("아이디/비밀번호/닉네임이 유효하지 않습니다")
                        }
                    }
                    mBinding?.signUpProgressBar?.visibility = View.GONE
                }
            },
            onFailure = {
                mBinding?.signUpProgressBar?.visibility = View.GONE
            }
        )
    }

    /**
     * 이메일 인증메일 요청 API 호출 메소드
     */
    private fun sendCertMail(inputEmail: String) {
        emailService.sendCertMail(
            inputEmail,
            onResponse = {},
            onErrorResponse = {},
            onFailure = {}
        )
    }

    /**
     * 비밀번호 입력 제한 체크 메소드.
     * 8자이상 20자이하의 숫자, 영어, 특수문자
     */
    private fun checkPasswordMatchCondition(){
        if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", mBinding?.signUpPasswordEt?.text.toString())) {
            isPasswordMatchCondition = true
            mBinding?.signUpPasswordErrorMessage?.visibility = View.GONE
        }
        else {
            isPasswordMatchCondition = false
            mBinding?.signUpPasswordErrorMessage?.visibility = View.VISIBLE
        }
    }

    /**
     * 모든 필드의 입력이 완료되고, 이메일, 닉네임 중복체크가 완료되면
     * 가입하기 버튼 활성화하는 메소드.
     */
    private fun changeConfirmButton() {
        if(isEmail && isPassword && isPasswordMatchCondition && isPasswordCheck && isNickname) {
            mBinding?.signUpSignUpBtn?.isEnabled = true
            mBinding?.signUpSignUpBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
        }
        else {
            mBinding?.signUpSignUpBtn?.isEnabled = false
            mBinding?.signUpSignUpBtn?.setBackgroundResource(R.drawable.confirmbtn_fail_background)
        }
    }


}