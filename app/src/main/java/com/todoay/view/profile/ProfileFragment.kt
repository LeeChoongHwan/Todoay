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
<<<<<<< HEAD
import com.todoay.MainActivity.Companion.mainAct
=======
import com.bumptech.glide.request.RequestOptions
>>>>>>> 44183944c17cfeded6349cc9347cb12673221633
import com.todoay.R
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.data.profile.Profile
import com.todoay.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var mBinding : FragmentProfileBinding?= null

    private val service by lazy { ProfileAPI.getInstance() }

    private lateinit var myProfile : Profile

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProfileBinding.inflate(inflater,container,false)

        mBinding = binding

        getUserProfile()

        // 뒤로가기 버튼
        mBinding?.profileBackBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

        //  Edit 버튼
        mBinding?.profileModifyinfoBtn?.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileModifyFragment(myProfile)
            Navigation.findNavController(requireView()).navigate(action)
        }

        return mBinding?.root
    }

    private fun getUserProfile() {
        service.getProfile(
            onResponse = {
                myProfile = Profile(
                    nickname = it.nickname,
                    introMsg = it.introMsg,
                    imageUrl = it.imageUrl
                )
                mBinding?.profileNicknameText?.text = myProfile.nickname
                mBinding?.profileMessageText?.text = myProfile.introMsg
                if(myProfile.imageUrl.isNullOrBlank() || myProfile.imageUrl == "null") {
                    Glide.with(mBinding!!.root)
                        .load(R.drawable.img_default_profile)
                        .into(mBinding?.profileImageBtn!!)
                } else {
                    Glide.with(mBinding!!.root)
                        .load(myProfile.imageUrl)
                        .apply(RequestOptions().circleCrop())
                        .error(R.drawable.img_default_profile)
                        .into(mBinding?.profileImageBtn!!)
                }
            },
            onErrorResponse = {
                /* 처리할 것 없음 */
            },
            onFailure = {
            }
        )
<<<<<<< HEAD

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
=======
>>>>>>> 44183944c17cfeded6349cc9347cb12673221633
    }

}