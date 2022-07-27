package com.todoay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.FragmentMyinfoBinding

class MyinfoFragment : Fragment() {

    private var mBinding : FragmentMyinfoBinding?= null

    private val profileService: ProfileAPI = ProfileAPI()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyinfoBinding.inflate(inflater,container,false)

        mBinding = binding

        profileService.getProfile(
            onResponse = {
                Log.d("profile-get", "onResponse() called in MyinfoFragment")
                mBinding?.myinfoJoinemailText?.text = it.email
                mBinding?.myinfoNicknameText?.text = it.nickname
                // 프로필 사진 세팅
                if(it.imageUrl!=null) {

                }
                // 상태메시지 세팅
                if(it.introMsg!=null) {
                    mBinding?.myinfoMessageText?.text = it.introMsg
                }
            },
            onErrorResponse = {
                // status 401 JWT 토큰 에러
                Log.d("profile-get", "onErrorResponse() called in MyinfoFragment")
                Toast.makeText(requireContext(), "로그인을 다시 해주세요", Toast.LENGTH_LONG).show()

            },
            onFailure = {
                Log.d("profile-get", "onFailure() called in MyinfoFragment")
                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
            }
        )



        mBinding?.myinfoModifyinfoBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_myinfoFragment_to_myinfoModifyFragment)
        }

        mBinding?.myinfoChangepasswordTv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_myinfoFragment_to_changePasswordFragment)
        }

        return mBinding?.root
    }
}