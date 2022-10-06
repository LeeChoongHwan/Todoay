package com.todoay.view.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.TodoayApplication
import com.todoay.api.domain.auth.AuthAPI
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.data.profile.Profile
import com.todoay.databinding.FragmentProfileModifyBinding
import com.todoay.global.util.PrintUtil.printLog
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.regex.Pattern

class ProfileModifyFragment : Fragment() {

    private var mBinding : FragmentProfileModifyBinding?= null

    var isModifyImageUrl : Boolean = false
    var isModifyNickname : Boolean = false
    var isModifyIntroMsg : Boolean = false

    private lateinit var myProfile : Profile

    private lateinit var currentNickname: String
    private var currentImageUrl: String? = null
    private var currentIntroMsg: String? = null

    private var imageFile : File? = null

    private val OPEN_GALLERY = 1

    private val authService by lazy { AuthAPI.getInstance() }
    private val profileService by lazy { ProfileAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProfileModifyBinding.inflate(inflater,container,false)

        mBinding = binding

        val args : ProfileModifyFragmentArgs by navArgs()
        myProfile = args.profile
        currentNickname = myProfile.nickname
        currentImageUrl = myProfile.imageUrl
        currentIntroMsg = myProfile.introMsg

        mBinding?.profileModifyNicknameEt?.setText(currentNickname)
        if(currentImageUrl.isNullOrBlank()) {
            Glide.with(mBinding!!.root)
                .load(R.drawable.img_default_profile)
                .into(mBinding?.profileModifyImageBtn!!)
        } else {
            Glide.with(mBinding!!.root)
                .load(currentImageUrl)
                .apply(RequestOptions().circleCrop())
                .error(R.drawable.img_default_profile)
                .into(mBinding?.profileModifyImageBtn!!)
        }
        mBinding?.profileModifyMessageEt?.setText(currentIntroMsg)

        /* 사진 변경 */
        mBinding?.profileModifyImageBtn?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
        }
        mBinding?.profileModifyImageTextBtn?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
        }
        mBinding?.profileModifyImageDeleteBtn?.setOnClickListener {
            Glide.with(mBinding!!.root)
                .load(R.drawable.img_default_profile)
                .into(mBinding?.profileModifyImageBtn!!)
            isModifyImageUrl = true
            imageFile = null
            currentImageUrl = null
        }

        /* 닉네임 필드 변경 */
        mBinding?.profileModifyNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(currentNickname == mBinding?.profileModifyNicknameEt?.text.toString()){
                    mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.GONE
                    isModifyNickname = false
                }
                changeConfirmButton()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputNickname = mBinding?.profileModifyNicknameEt?.text.toString()
                if(inputNickname != "") {
                    // 현재 닉네임과 동일한 경우
                    if(currentNickname == inputNickname){
                        mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.GONE
                        isModifyNickname = false
                    }
                    else {
                        if(!inputNickname.contains(" ") && Pattern.matches("^[a-zA-Z0-9_]*\$", inputNickname)) {
                            checkNicknameExists(inputNickname)
                        }
                        else {
                            mBinding?.profileModifyNicknameAlertMsgTv?.text = "공백 또는 특수문자('_'제외)를 입력할 수 없습니다"
                            mBinding?.profileModifyNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                            mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.VISIBLE
                            isModifyNickname = false
                            changeConfirmButton()
                        }
                    }
                }
                else {
                    mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.GONE
                    isModifyNickname = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 상태메시지 변경 */
        mBinding?.profileModifyMessageEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isModifyIntroMsg = currentIntroMsg != mBinding?.profileModifyMessageEt?.text.toString()
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 뒤로가기 버튼 */
        mBinding?.profileModifyBackbtn?.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

        /* 확인 버튼 */
        mBinding?.profileModifyConfirmBtn?.setOnClickListener {
            changeConfirmBtnEnabled(false)

            myProfile.nickname = mBinding?.profileModifyNicknameEt?.text.toString()
            myProfile.imageUrl = currentImageUrl
            myProfile.introMsg = mBinding?.profileModifyMessageEt?.text.toString()

            profileService.putProfile(
                imageFile,
                myProfile,
                onResponse = {
                    Navigation.findNavController(requireView()).navigate(R.id.action_profileModifyFragment_to_profileFragment)
                },
                onErrorResponse = {
                    changeConfirmBtnEnabled(true)
                    /*
                     * 400 유효성 검사 실패
                     * 403 허용되지 않은 접근
                     */
                    if(it.status == 400 && it is ValidErrorResponse) {
                        when(it.details[0].field) {
                            "nickname" -> {
                                mBinding?.profileModifyNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                                mBinding?.profileModifyNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                                mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.VISIBLE
                                mBinding?.profileModifyNicknameEt?.requestFocus()
                                isModifyNickname = false
                                changeConfirmButton()
                            }
                            "introMsg" -> {
                                mainAct!!.showShortToast("상태메시지를 수정해주세요")
                                mBinding?.profileModifyMessageEt?.setText("")
                                mBinding?.profileModifyMessageEt?.requestFocus()
                                isModifyIntroMsg = false
                                changeConfirmButton()
                            }
                            "imageUrl" -> {
                                mainAct!!.showShortToast("프로필 사진을 수정해주세요")
                                isModifyImageUrl = false
                                changeConfirmButton()
                            }
                        }
                    }
                },
                onFailure = {
                    changeConfirmBtnEnabled(true)
                }
            )
        }

        return mBinding?.root
    }

    private fun changeConfirmBtnEnabled(isEnabled: Boolean) {
        mBinding?.profileModifyConfirmBtn?.isEnabled = isEnabled
    }

    /**
     * 닉네임 중복확인 API 호출 메소드
     */
    private fun checkNicknameExists(inputNickname: String) {
        authService.checkNicknameExists(
            inputNickname,
            onResponse = {
                if (!it.nicknameExist) {
                    mBinding?.profileModifyNicknameAlertMsgTv?.text = "사용할 수 있는 닉네임입니다"
                    mBinding?.profileModifyNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isModifyNickname = true
                    changeConfirmButton()
                }
                else {
                    mBinding?.profileModifyNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                    mBinding?.profileModifyNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                    mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isModifyNickname = false
                    changeConfirmButton()
                }
            },
            onErrorResponse = {
                // status 400 유효성 검사 실패
                if(it.details[0].code == "NotBlank" && it.details[0].field == "nickname") {
                    mBinding?.profileModifyNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                    mBinding?.profileModifyNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                    mBinding?.profileModifyNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isModifyNickname = false
                    changeConfirmButton()
                }
            },
            onFailure = {
            }
        )
    }

    private fun changeConfirmButton() {
        if(isModifyNickname || isModifyIntroMsg || isModifyImageUrl) {
            mBinding?.profileModifyConfirmBtn?.setTextColor(resources.getColor(R.color.main_color))
            mBinding?.profileModifyConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.profileModifyConfirmBtn?.setTextColor(resources.getColor(R.color.gray))
            mBinding?.profileModifyConfirmBtn?.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Glide Version
        when(requestCode) {
            OPEN_GALLERY -> {
                val selectedImageUri = data?.data!!
                printLog("[프로필 사진 수정] 이미지 URL : $selectedImageUri")
                requireActivity().contentResolver.openInputStream(selectedImageUri)!!.use { inputStream ->
                    imageFile = createJpegFile(requireContext(), "${TodoayApplication.pref.getEmail()}_${System.currentTimeMillis()}")
                    inputStreamToFile(inputStream, imageFile!!)
                }
                Glide.with(requireView())
                    .load(selectedImageUri)
                    .apply(RequestOptions().circleCrop())
                    .into(mBinding?.profileModifyImageBtn!!)
                isModifyImageUrl = true
                changeConfirmButton()
            }
            Activity.RESULT_CANCELED -> {
                mainAct!!.showShortToast("사진 선택 취소")
            }
        }

        // Origin Version
//        if (requestCode == OPEN_GALLERY) {
//            if (resultCode == Activity.RESULT_OK) {
//                val currentImageUri = data?.data
//                requireActivity().contentResolver.openInputStream(currentImageUri!!)?.use {
//                    inputStream ->
//                    val file = createFile(requireContext(), "temp", "jpg")
//                    inputStreamToFile(inputStream, file)
//                }
//
//                try {
//                    currentImageUri.let {
//                        if (Build.VERSION.SDK_INT < 28) {
//                            val bitmap = MediaStore.Images.Media.getBitmap(
//                                requireActivity().contentResolver,
//                                currentImageUri
//                            )
//                            mBinding?.profileModifyAddpictureBtn?.setImageBitmap(bitmap)
//                        }
//                        else {
//                            val source = ImageDecoder.createSource(
//                                requireActivity().contentResolver,
//                                currentImageUri
//                            )
//                            val bitmap = ImageDecoder.decodeBitmap(source)
//                            mBinding?.profileModifyAddpictureBtn?.setImageBitmap(bitmap)
//                        }
//                    }
//                } catch(e: IOException) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//        else if (resultCode == Activity.RESULT_CANCELED) {
//            Toast.makeText(requireActivity(), "사진 선택 취소", Toast.LENGTH_LONG).show()
//        }
    }

    private fun createJpegFile(context: Context, fileName: String?): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File(storageDir, "$fileName.jpg")
    }

    private fun inputStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
    }
}