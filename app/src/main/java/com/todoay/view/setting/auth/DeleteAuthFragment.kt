package com.todoay.view.setting.auth

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.api.domain.auth.AuthAPI
import com.todoay.api.domain.auth.AuthService
import com.todoay.api.domain.auth.dto.request.DeleteAuthRequest
import com.todoay.databinding.FragmentDeleteAuthBinding
import com.todoay.global.util.PrintUtil.printLog
import java.util.regex.Pattern

class DeleteAuthFragment : Fragment() {

    lateinit var binding : FragmentDeleteAuthBinding

    private var isPasswordMatchCondition : Boolean = false

    private val service : AuthAPI by lazy { AuthAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeleteAuthBinding.inflate(inflater, container, false)

        /* 뒤로가기 버튼 */
        binding.deleteAuthBackbtn.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

        /* content title "삭제" span */
        val contentTitle = binding.deleteAuthContentTitle.text.toString()
        val spanBuilder = SpannableStringBuilder(contentTitle)
        val redColorSpan = ForegroundColorSpan(resources.getColor((R.color.red)))
        spanBuilder.setSpan(redColorSpan, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.deleteAuthContentTitle.text = spanBuilder

        /* 비밀번호 et */
        binding.deleteAuthPasswordEt.addTextChangedListener(object : TextWatcher {
            private lateinit var password : String
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = binding.deleteAuthPasswordEt.text.toString()
                if(password.isNotEmpty()) {
                    checkPasswordMatchCondition(password)
                } else {
                    isPasswordMatchCondition = false
                    binding.deleteAuthPasswordErrorMessage.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        /* 삭제하기 버튼 */
        binding.deleteAuthConfirmBtn.setOnClickListener {
            val password = binding.deleteAuthPasswordEt.text.toString()
            val request =  DeleteAuthRequest(password)
            service.withdrawal(
                request,
                onResponse = {
                    mainAct.withdrawal()
                },
                onErrorResponse = {
                    mainAct.showShortToast("계정 삭제가 실패하였습니다\n다시 시도해주세요")
                },
                onFailure = {}
            )
        }

        return binding.root
    }

    private fun checkPasswordMatchCondition(password: String){
        if(password.length >= 8) {
            isPasswordMatchCondition = true
            binding.deleteAuthPasswordErrorMessage.visibility = View.GONE
        } else {
            isPasswordMatchCondition = false
            binding.deleteAuthPasswordErrorMessage.visibility = View.VISIBLE
        }
        changeConfirmButton()
    }

    private fun changeConfirmButton() {
        if(isPasswordMatchCondition) {
            binding.deleteAuthConfirmBtn.isEnabled = true
            binding.deleteAuthConfirmBtn.setBackgroundResource(R.drawable.bg_primary_btn)
        }
        else {
            binding.deleteAuthConfirmBtn.isEnabled = false
            binding.deleteAuthConfirmBtn.setBackgroundResource(R.drawable.bg_primary_fail_btn)
        }
    }

}