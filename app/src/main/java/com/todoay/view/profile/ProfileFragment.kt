package com.todoay.view.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.api.config.RetrofitService
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.FragmentProfileBinding
import com.todoay.global.util.TodoayApplication
import com.todoay.global.util.UserLogout

class ProfileFragment : Fragment() {

    private var mBinding : FragmentProfileBinding?= null

    private val profileService: ProfileAPI = ProfileAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("profile", "onCreate() called for profile-get API in profileFragment")
        profileService.getProfile(
            onResponse = {
                Log.d("profile-get", "onResponse() called in profileFragment")
                mBinding?.profileJoinemailText?.text = it.email
                mBinding?.profileNicknameText?.text = it.nickname
                // 프로필 사진 세팅
                if(it.imageUrl!=null) {

                }
                // 상태메시지 세팅
                if(it.introMsg!=null) {
                    mBinding?.profileMessageText?.text = it.introMsg
                }
            },
            onErrorResponse = {
                // status 401 JWT 토큰 에러
                Log.d("profile-get", "onErrorResponse() called in profileFragment")



                Toast.makeText(requireContext(), "로그인을 다시 해주세요", Toast.LENGTH_LONG).show()

            },
            onFailure = {
                Log.d("profile-get", "onFailure() called in profileFragment")
                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("profile", "onCreateView() called in profileFragment")

        val binding = FragmentProfileBinding.inflate(inflater,container,false)

        mBinding = binding

        // 메뉴바 버튼
        // Test를 위해 로그아웃 진행
        mBinding?.profileMenubtn?.setOnClickListener {
            UserLogout.logout()
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment)
        }

        //  Edit 버튼
        mBinding?.profileModifyinfoBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_profileModifyFragment)
        }

        // 비밀번호 변경 버튼
        mBinding?.profileChangepasswordTv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        return mBinding?.root
    }
}