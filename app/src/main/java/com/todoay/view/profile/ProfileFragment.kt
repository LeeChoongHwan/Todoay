package com.todoay.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

//        //갤러리 이미지 추가
//        mBinding?.profileAddpictureBtn?.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.type= "image/*"
//
//            val getGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                if (it.resultCode == RESULT_OK) {
//                    Glide.with(this)
//                        .load(it.data!!.data)
//                        .override(200,200)
//                        .into(mBinding?.profileAddpictureBtn!!)
//                }
//            }
//
//        }

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

    }

}