package com.todoay.view.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var mBinding : FragmentProfileBinding?= null

    private val service by lazy { ProfileAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProfileBinding.inflate(inflater,container,false)

        mBinding = binding

        service.getProfile(
            onResponse = {
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
                /* 처리할 것 없음 */
            },
            onFailure = {
            }
        )

        // 메뉴바 버튼
        // Test를 위해 로그아웃 진행
        mBinding?.profileBackBtn?.setOnClickListener {
            mainAct.logout()
        }

        //  Edit 버튼
        mBinding?.profileModifyinfoBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_profileModifyFragment)
        }

        // 비밀번호 변경 버튼
        mBinding?.profileChangepasswordTv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
        
        //갤러리 이미지 추가
        mBinding?.profileAddpictureBtn?.setOnClickListener { 
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type= "image/*"

            val getGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    Glide.with(this)
                        .load(it.data!!.data)
                        .override(200,200)
                        .into(mBinding?.profileAddpictureBtn!!)
                }
            }

        }

        return mBinding?.root
    }

}