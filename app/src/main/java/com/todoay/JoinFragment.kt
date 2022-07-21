package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentEmailCertificationFindPasswordBinding
import com.todoay.databinding.FragmentJoinBinding
import java.util.regex.Pattern

class JoinFragment : Fragment() {

    private var mBinding : FragmentJoinBinding?= null

    var isPassword : Boolean = false
    var isPasswordCheck : Boolean = false
    var isNickname : Boolean = false
    var isCondition : Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentJoinBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.joinEmailEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        //비밀번호 입력 필드
        mBinding?.joinPasswordEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(mBinding?.joinPasswordEt?.text.toString() != "") {
                    checkConditon()
                    isPassword = true
                }
                else {
                    checkConditon()
                    isPassword = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.joinPasswordEt?.text.toString() != "") {
                    checkConditon()
                    isPassword = true
                }
                else {
                    checkConditon()
                    isPassword = false
                }
                changeConfirmButton()
            }

        })

        //비밀번호 확인 필드
        mBinding?.joinPasswordcheckEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.joinPasswordEt?.text.toString() == mBinding?.joinPasswordcheckEt?.text?.toString()) {
                    isPasswordCheck = true
                    mBinding?.joinPasswordcheckErrormessage?.visibility = View.GONE
                }
                else {
                    mBinding?.joinPasswordcheckErrormessage?.visibility = View.VISIBLE
                    isPasswordCheck = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {

                if(mBinding?.joinPasswordEt?.text.toString() == mBinding?.joinPasswordcheckEt?.text?.toString()) {
                    isPasswordCheck = true
                    mBinding?.joinPasswordcheckErrormessage?.visibility = View.GONE
                }
                else {
                    mBinding?.joinPasswordcheckErrormessage?.visibility = View.VISIBLE
                    isPasswordCheck = false
                }
                changeConfirmButton()
            }

        })

        //닉네임 필드
        mBinding?.joinNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.joinNicknameEt?.text?.toString() != "") {
                    mBinding?.joinNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.joinNicknamecheckBtn?.isEnabled = true
                }
                else {
                    mBinding?.joinNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.joinNicknamecheckBtn?.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.joinNicknameEt?.text?.toString() != "") {
                    mBinding?.joinNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.joinNicknamecheckBtn?.isEnabled = true
                }
                else {
                    mBinding?.joinNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.joinNicknamecheckBtn?.isEnabled = false
                }
            }

        })
        //닉네임 중복확인 버튼
        mBinding?.joinNicknamecheckBtn?.setOnClickListener {
            mBinding?.joinNicknameErrormessage?.visibility = View.VISIBLE
            isNickname = true
            changeConfirmButton()
        }

        //뒤로가기 버튼
        mBinding?.joinBackBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_joinFragment_to_emailCertificationFragment2)
        }

        //회원가입 버튼
        mBinding?.joinConfirmBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_joinFragment_to_loginFragment)
        }

        return mBinding?.root
    }

    private fun checkConditon( ){
        if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",
                mBinding?.joinPasswordEt?.text.toString())) {
            isCondition = true
            mBinding?.joinPasswordErrormessage?.visibility = View.GONE
        }
        else {
            isCondition = false
            mBinding?.joinPasswordErrormessage?.visibility = View.VISIBLE

        }
    }

    private fun changeConfirmButton() {
        if(isPassword && isCondition && isPasswordCheck && isNickname == true) {
            mBinding?.joinConfirmBtn?.isEnabled = true
            mBinding?.joinConfirmBtn?.setBackgroundResource(R.drawable.confirmbtn_background)
        }
        else {
            mBinding?.joinConfirmBtn?.isEnabled = false
            mBinding?.joinConfirmBtn?.setBackgroundResource(R.drawable.confirmbtn_fail_background)
        }
    }
}