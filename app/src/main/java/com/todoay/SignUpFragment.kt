package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.api.domain.auth.email.EmailAPI
import com.todoay.api.domain.auth.nickName.NickNameAPI
import com.todoay.api.domain.auth.signUp.SignUpAPI
import com.todoay.databinding.FragmentSignUpBinding
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    private var mBinding : FragmentSignUpBinding?= null

    var isEmail : Boolean = false
    var isPassword : Boolean = false
    var isPasswordCheck : Boolean = false
    var isNickname : Boolean = false
    var isPasswordMatchCondition : Boolean = false

    var inputEmail : String = ""
    var inputPassword : String = ""
    var inputNickName : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSignUpBinding.inflate(inflater,container,false)

        mBinding = binding


        /**
         * 이메일 필드 입력
         */
        mBinding?.signUpEmailEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 이메일 editText가 공백이 아닐 경우
                if(mBinding?.signUpEmailEt?.text.toString() != "") {
                    // 이메일 형식 패턴 매칭
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.signUpEmailEt?.text.toString()).matches()) {
                        mBinding?.signUpEmailCheckBtn?.isEnabled = true
                        mBinding?.signUpEmailCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                        mBinding?.signUpEmailCheckBtn?.setTextColor(resources.getColor(R.color.main_color))
                        mBinding?.signUpEmailValidTv?.visibility = View.GONE
                    }
                    else {
                        mBinding?.signUpEmailCheckBtn?.isEnabled = false
                        mBinding?.signUpEmailCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                        mBinding?.signUpEmailCheckBtn?.setTextColor(resources.getColor(R.color.gray))
                        mBinding?.signUpEmailValidTv?.visibility = View.VISIBLE
                    }
                    //
                    if(mBinding?.signUpEmailCheckPossibleMessageTv?.visibility == View.VISIBLE || mBinding?.signUpEmailCheckErrorMessageTv?.visibility == View.VISIBLE){
                        mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.GONE
                        mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.GONE
                    }
                }
                // 이메일 editText가 공백일 경우
                else {
                    mBinding?.signUpEmailCheckBtn?.isEnabled = false
                    mBinding?.signUpEmailCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.signUpEmailCheckBtn?.setTextColor(resources.getColor(R.color.gray))
                    mBinding?.signUpEmailValidTv?.visibility = View.GONE
                    mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.GONE
                    mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // 이메일 editText가 공백이 아닐 경우
                if(mBinding?.signUpEmailEt?.text.toString() != "") {
                    // 이메일 형식 패턴 매칭
                    if(Patterns.EMAIL_ADDRESS.matcher(mBinding?.signUpEmailEt?.text.toString()).matches()) {
                        mBinding?.signUpEmailCheckBtn?.isEnabled = true
                        mBinding?.signUpEmailCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                        mBinding?.signUpEmailCheckBtn?.setTextColor(resources.getColor(R.color.main_color))
                        mBinding?.signUpEmailValidTv?.visibility = View.GONE
                    }
                    else {
                        mBinding?.signUpEmailCheckBtn?.isEnabled = false
                        mBinding?.signUpEmailCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                        mBinding?.signUpEmailCheckBtn?.setTextColor(resources.getColor(R.color.gray))
                        mBinding?.signUpEmailValidTv?.visibility = View.VISIBLE
                    }
                    if(mBinding?.signUpEmailCheckPossibleMessageTv?.visibility == View.VISIBLE || mBinding?.signUpEmailCheckErrorMessageTv?.visibility == View.VISIBLE){
                        mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.GONE
                        mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.GONE
                    }
                }
                // 이메일 editText가 공백일 경우
                else {
                    mBinding?.signUpEmailCheckBtn?.isEnabled = false
                    mBinding?.signUpEmailCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.signUpEmailCheckBtn?.setTextColor(resources.getColor(R.color.gray))
                    mBinding?.signUpEmailValidTv?.visibility = View.GONE
                    mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.GONE
                    mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.GONE
                }
            }

        })

        /**
         * 이메일 중복확인 버튼
         */
        mBinding?.signUpEmailCheckBtn?.setOnClickListener {
            // 테스트 코드. 추후 API 연동하면 삭제 요망.
            mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.VISIBLE
            isEmail = true
            changeConfirmButton()

            // 이메일 중복확인 API 호출
            /*
            EmailAPI().checkEmailDuplicate(
                mBinding?.signUpEmailEt?.text.toString(),
                onResponse = {
                    mBinding?.signUpEmailCheckPossibleMessageTv?.visibility = View.VISIBLE
                    isEmail = true
                    changeConfirmButton()
                },
                onErrorResponse = {
                    mBinding?.signUpEmailCheckErrorMessageTv?.visibility = View.VISIBLE
                    isEmail = false
                    changeConfirmButton()
                },
                onFailure = {

                }
            )
             */
        }

        /**
         * 비밀번호 필드 입력
         */
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

        })

        /**
         * 비밀번호 확인 필드 입력
         */
        mBinding?.signUpPasswordCheckEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.signUpPasswordEt?.text.toString() == mBinding?.signUpPasswordCheckEt?.text?.toString()) {
                    isPasswordCheck = true
                    mBinding?.signUpPasswordCheckErrorMessage?.visibility = View.GONE
                }
                else {
                    mBinding?.signUpPasswordCheckErrorMessage?.visibility = View.VISIBLE
                    isPasswordCheck = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.signUpPasswordEt?.text.toString() == mBinding?.signUpPasswordCheckEt?.text?.toString()) {
                    isPasswordCheck = true
                    mBinding?.signUpPasswordCheckErrorMessage?.visibility = View.GONE
                }
                else {
                    mBinding?.signUpPasswordCheckErrorMessage?.visibility = View.VISIBLE
                    isPasswordCheck = false
                }
                changeConfirmButton()
            }

        })

        /**
         * 닉네임 필드 입력
         */
        mBinding?.signUpNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.signUpNicknameEt?.text?.toString() != "") {
                    mBinding?.signUpNicknameCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.signUpNicknameCheckBtn?.isEnabled = true
                    mBinding?.signUpNicknameCheckBtn?.setTextColor(resources.getColor(R.color.main_color))
                }
                else {
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.GONE
                    mBinding?.signUpNicknameCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.signUpNicknameCheckBtn?.isEnabled = false
                    mBinding?.signUpNicknameCheckBtn?.setTextColor(resources.getColor(R.color.gray))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.signUpNicknameEt?.text?.toString() != "") {
                    mBinding?.signUpNicknameCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.signUpNicknameCheckBtn?.isEnabled = true
                    mBinding?.signUpNicknameCheckBtn?.setTextColor(resources.getColor(R.color.main_color))
                }
                else {
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.GONE
                    mBinding?.signUpNicknameCheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.signUpNicknameCheckBtn?.isEnabled = false
                    mBinding?.signUpNicknameCheckBtn?.setTextColor(resources.getColor(R.color.gray))
                }
            }

        })

        /**
         * 닉네임 중복확인 버튼
         */
        mBinding?.signUpNicknameCheckBtn?.setOnClickListener {
            // 닉네임 중복확인 API 연동 이전의 테스트를 위한 코드. API 연동 이후 삭제 요망.
            mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 있는 닉네임입니다"
            mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.green))
            mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
            isNickname = true
            changeConfirmButton()

            // 닉네임 중복확인 API 호출
            /*
            NickNameAPI().checkNickNameDuplicate(
                mBinding?.signUpNicknameEt?.text.toString(),
                onResponse = {
                    mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 있는 닉네임입니다"
                    mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isNickname = true
                    changeConfirmButton()
                },
                onErrorResponse = {
                    mBinding?.signUpNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                    mBinding?.signUpNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                    mBinding?.signUpNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isNickname = false
                    mBinding?.signUpNicknameEt?.requestFocus()
                },
                onFailure = {

                }
            )
             */
        }

        /**
         * 뒤로가기 버튼
         */
        mBinding?.signUpBackBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        /**
         * 가입하기 버튼
         */
        mBinding?.signUpSignUpBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_joinFragment_to_signUpEmailCertAlertFragment)

            // 버튼 클릭 시 서버 API 호출? -> Auth 객체를 생성하기 위한?
            /*
            SignUpAPI().signUp(
                inputEmail,
                inputPassword,
                inputNickName,
                onResponse = {

                    Navigation.findNavController(requireView()).navigate(R.id.action_joinFragment_to_signUpEmailCertAlertFragment)
                },
                onErrorResponse = {

                },
                onFailure = {

                }
            )
             */
        }

        return mBinding?.root
    }

    /**
     * 비밀번호 입력 제한 체크 메소드.
     * 8자이상 20자이하의 숫자, 영어, 특수문자
     */
    private fun checkPasswordMatchCondition(){
        if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",
                mBinding?.signUpPasswordEt?.text.toString())) {
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
            inputEmail = mBinding?.signUpEmailEt?.text.toString()
            inputPassword = mBinding?.signUpPasswordEt?.text.toString()
            inputNickName = mBinding?.signUpNicknameEt?.text.toString()
        }
        else {
            mBinding?.signUpSignUpBtn?.isEnabled = false
            mBinding?.signUpSignUpBtn?.setBackgroundResource(R.drawable.confirmbtn_fail_background)
        }
    }
}