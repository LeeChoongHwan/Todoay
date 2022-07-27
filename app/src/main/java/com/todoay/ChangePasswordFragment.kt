package com.todoay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentChangePasswordBinding
import com.todoay.databinding.FragmentEmailCertificationBinding
import java.util.regex.Pattern

class ChangePasswordFragment : Fragment() {

    private var mBinding : FragmentChangePasswordBinding?= null

    var isPresentPassword : Boolean = false
    var isChangedPassword : Boolean = false
    var isChangedCheckPassword : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentChangePasswordBinding.inflate(inflater,container,false)

        mBinding = binding

        //뒤로가기 버튼
        mBinding?.changepasswordBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_changePasswordFragment_to_myinfoFragment)
        }

        //툴바 확인 버튼
        mBinding?.changepasswordToolbarConfirmBtn?.setOnClickListener {
            //비밀번호 변경 API 호출

            /*
            if(mBinding?.changepasswordPresentpasswordEt?.text.toString() == "12345678") {
                Navigation.findNavController(view!!)
                    .navigate(R.id.action_changePasswordFragment_to_myinfoFragment)
            }
            else {
                mBinding?.changepasswordErrorMessage?.visibility = View.VISIBLE
            }
             */
        }

        //현재 비밀번호 edittext
        mBinding?.changepasswordPresentpasswordEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isPresentPassword = mBinding?.changepasswordPresentpasswordEt?.text.toString() != ""
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
                        mBinding?.changepasswordChangedpassowordcheckEt?.text.toString()) {
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
        if(isChangedCheckPassword && isChangedPassword && isPresentPassword) {
            mBinding?.changepasswordToolbarConfirmBtn?.setTextColor(R.color.main_color)
            mBinding?.changepasswordToolbarConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.changepasswordToolbarConfirmBtn?.setTextColor(R.color.gray)
            mBinding?.changepasswordToolbarConfirmBtn?.isEnabled = false
        }
    }

}