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
import com.todoay.databinding.FragmentChangeForgetPasswordBinding
import java.util.regex.Pattern

class ChangeForgetPasswordFragment : Fragment() {

    private var mBinding : FragmentChangeForgetPasswordBinding? = null
    
    private var isChangedPassword: Boolean = false
    private var isChangedCheckPassword: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentChangeForgetPasswordBinding.inflate(inflater)

        mBinding = binding

        mBinding?.changeForgetPasswordChangedpassowordEt?.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.changeForgetPasswordChangedpassowordEt?.text.toString() != "") {
                    if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",mBinding?.changeForgetPasswordChangedpassowordEt?.text.toString())) {
                        isChangedPassword = true
                        mBinding?.changeForgetPasswordchangeErrorMessage?.visibility = View.GONE
                    }
                    else {
                        isChangedPassword = false
                        mBinding?.changeForgetPasswordchangeErrorMessage?.visibility = View.VISIBLE
                    }
                }
                else {
                    isChangedPassword = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        mBinding?.changeForgetPasswordChangedpassowordcheckEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.changeForgetPasswordChangedpassowordcheckEt?.text.toString() != "") {
                    if(mBinding?.changeForgetPasswordChangedpassowordcheckEt?.text.toString() ==
                        mBinding?.changeForgetPasswordChangedpassowordcheckEt?.text.toString()) {
                        mBinding?.changeForgetPasswordcheckErrorMessage?.visibility = View.GONE
                        isChangedCheckPassword = true
                    }
                    else {
                        mBinding?.changeForgetPasswordcheckErrorMessage?.visibility = View.VISIBLE
                        isChangedCheckPassword = false
                    }
                }
                else {
                    isChangedCheckPassword = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        //뒤로가기 버튼
        mBinding?.changeForgetPasswordBackbtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_changeForgetPasswordFragment_to_loginFragment)
        }

        //툴바 확인 버튼
        mBinding?.changeForgetPasswordToolbarConfirmBtn?.setOnClickListener {
            // 비밀번호 변경 API 호출

        }

        return mBinding?.root
    }
    
    private fun changeConfirmButton() {
        if(isChangedCheckPassword && isChangedPassword) {
            mBinding?.changeForgetPasswordToolbarConfirmBtn?.setTextColor(resources.getColor(R.color.main_color))
            mBinding?.changeForgetPasswordToolbarConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.changeForgetPasswordToolbarConfirmBtn?.setTextColor(resources.getColor(R.color.gray))
            mBinding?.changeForgetPasswordToolbarConfirmBtn?.isEnabled = false
        }
    }
}