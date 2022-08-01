package com.todoay.view.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.api.domain.auth.password.ModifyPasswordAPI
import com.todoay.databinding.FragmentChangePasswordBinding
import com.todoay.global.util.UserAccount
import java.util.regex.Pattern

class ChangePasswordFragment : Fragment() {

    private var mBinding : FragmentChangePasswordBinding?= null

    var isOriginPassword : Boolean = false
    var isChangedPassword : Boolean = false
    var isChangedCheckPassword : Boolean = false

    val modifyPasswordService = ModifyPasswordAPI()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentChangePasswordBinding.inflate(inflater,container,false)

        mBinding = binding

        //뒤로가기 버튼
        mBinding?.changepasswordBackbtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_changePasswordFragment_to_profileFragment)
        }

        //툴바 확인 버튼
        mBinding?.changepasswordToolbarConfirmBtn?.setOnClickListener {
            val originPassword = mBinding?.changepasswordOriginPasswordEt?.text.toString()
            val modifiedPassword = mBinding?.changepasswordChangedpassowordEt?.text.toString()

            //비밀번호 변경 API 호출
            modifyPasswordService.modifyPassword(
                originPassword,
                modifiedPassword,
                onResponse = {
                    Toast.makeText(requireContext(), "다시 로그인해주세요", Toast.LENGTH_LONG).show()
                    UserAccount.logout()
                    Navigation.findNavController(requireView()).navigate(R.id.action_changePasswordFragment_to_loginFragment)
                },
                onErrorResponse = {
                    // status 404 비밀번호 양식 유효성 실패
                    mBinding?.changepasswordOriginPasswordErrorMessage?.visibility = View.VISIBLE
                    mBinding?.changepasswordOriginPasswordEt?.setText("")
                    mBinding?.changepasswordOriginPasswordEt?.requestFocus()
                },
                onFailure = {
                    Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                }
            )
        }

        //현재 비밀번호 edittext
        mBinding?.changepasswordOriginPasswordEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isOriginPassword = mBinding?.changepasswordOriginPasswordEt?.text.toString() != ""
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        //변경 비밀번호 edittext
        mBinding?.changepasswordChangedpassowordEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.changepasswordChangedpassowordEt?.text.toString() != "") {
                    if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",mBinding?.changepasswordChangedpassowordEt?.text.toString())) {
                        isChangedPassword = true
                        mBinding?.changepasswordchangeErrorMessage?.visibility = View.GONE
                    }
                    else {
                        isChangedPassword = false
                        mBinding?.changepasswordchangeErrorMessage?.visibility = View.VISIBLE
                    }
                }
                else {
                    mBinding?.changepasswordchangeErrorMessage?.visibility = View.GONE
                    isChangedPassword = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        //변경 비밀번호 확인 edittext
        mBinding?.changepasswordChangedpassowordcheckEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.changepasswordChangedpassowordcheckEt?.text.toString() != "") {
                    if(mBinding?.changepasswordChangedpassowordcheckEt?.text.toString() ==
                        mBinding?.changepasswordChangedpassowordEt?.text.toString()) {
                        mBinding?.changepasswordcheckErrorMessage?.visibility = View.GONE
                        isChangedCheckPassword = true
                    }
                    else {
                        mBinding?.changepasswordcheckErrorMessage?.visibility = View.VISIBLE
                        isChangedCheckPassword = false
                    }
                }
                else {
                    mBinding?.changepasswordcheckErrorMessage?.visibility = View.GONE
                    isChangedCheckPassword = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return mBinding?.root
    }


    private fun changeConfirmButton() {
        if(isChangedCheckPassword && isChangedPassword && isOriginPassword) {
            mBinding?.changepasswordToolbarConfirmBtn?.setTextColor(resources.getColor(R.color.main_color))
            mBinding?.changepasswordToolbarConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.changepasswordToolbarConfirmBtn?.setTextColor(resources.getColor(R.color.gray))
            mBinding?.changepasswordToolbarConfirmBtn?.isEnabled = false
        }
    }

}