package com.todoay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.api.domain.auth.nickname.NicknameAPI
import com.todoay.api.domain.auth.nickname.NicknameService
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.FragmentMyinfoBinding
import com.todoay.databinding.FragmentMyinfoModifyBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class MyinfoModifyFragment : Fragment() {

    private var mBinding : FragmentMyinfoModifyBinding?= null

    var isModifyImageUrl : Boolean = false
    var isModifyNickname : Boolean = false
    var isModifyIntroMsg : Boolean = false

    var currentImageUrl: String = ""
    var currentNickname: String = ""
    var currentIntroMsg: String = ""

    private var OPEN_GALLERY = 1

    private val profileService: ProfileAPI = ProfileAPI()
    private val nicknameService : NicknameAPI = NicknameAPI()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyinfoModifyBinding.inflate(inflater,container,false)

        mBinding = binding

        profileService.getProfile(
            onResponse = {
                Log.d("profile-get", "onResponse() called in MyinfoModifyFragment")
                currentNickname = it.nickname
                mBinding?.myinfomodifyNicknameEt?.setText(currentNickname)
                // 프로필 사진 세팅
                if(it.imageUrl!=null) {
                    currentImageUrl = it.imageUrl

                }
                // 상태메시지 세팅
                if(it.introMsg!=null) {
                    currentIntroMsg = it.introMsg
                    mBinding?.myinfomodifyMessageEt?.setText(currentIntroMsg)
                }
            },
            onErrorResponse = {
                // status 401 JWT 토큰 에러
                Log.d("profile-get", "onErrorResponse() called in MyinfoModifyFragment")
                Toast.makeText(requireContext(), "로그인을 다시 해주세요", Toast.LENGTH_LONG).show()
            },
            onFailure = {
                Log.d("profile-get", "onFailure() called in MyinfoModifyFragment")
                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
            }
        )

        /**
         * 닉네임 필드 변경
         */
        mBinding?.myinfomodifyNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(currentNickname == mBinding?.myinfomodifyNicknameEt?.text.toString()){
                    mBinding?.myinfoNicknameAlertMsgTv?.visibility = View.GONE
                    isModifyNickname = false
                }
                changeConfirmButton()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.myinfomodifyNicknameEt?.text.toString() != "") {
                    // 현재 닉네임과 동일한 경우
                    if(currentNickname == mBinding?.myinfomodifyNicknameEt?.text.toString()){
                        mBinding?.myinfoNicknameAlertMsgTv?.visibility = View.GONE
                        isModifyNickname = false
                    }
                    else {
                        // API 호출
                        checkNicknameExists(mBinding?.myinfomodifyNicknameEt?.text.toString())
                    }
                }
                else {
                    mBinding?.myinfoNicknameAlertMsgTv?.visibility = View.GONE
                    isModifyNickname = false
                }
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        mBinding?.myinfomodifyMessageEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isModifyIntroMsg = currentIntroMsg != mBinding?.myinfomodifyMessageEt?.text.toString()
                changeConfirmButton()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /**
         * 뒤로가기 버튼
         */
        mBinding?.myinfomodifyBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
        }

        /**
         * 확인 버튼
         */
        mBinding?.myinfoConfirmBtn?.setOnClickListener {
            val inputImageUrl = ""
            val inputNickname = mBinding?.myinfomodifyNicknameEt?.text.toString()
            val inputIntroMsg = mBinding?.myinfomodifyMessageEt?.text.toString()
            // 프로필 수정 API 호출
            profileService.putProfile(
                inputNickname,
                inputIntroMsg,
                inputImageUrl,
                onResponse = {
                    Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
                },
                onErrorResponse = {
                    // Status 400 유효성 검사 실패
                    // Status 401 JWT 토큰 에러

                },
                onFailure = {
                    Log.d("put-profile", "onFailure() called in MyinfoModifyFragment")
                    Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                }
            )

        }

        //사진
        mBinding?.myinfomodifyAddpictureBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent,OPEN_GALLERY)
            }
        })

        return mBinding?.root
    }

    /**
     * 닉네임 중복확인 API 호출 메소드
     */
    private fun checkNicknameExists(inputNickname: String) {
        nicknameService.checkNicknameExists(
            inputNickname,
            onResponse = {
                if (!it.nicknameExist) {
                    Log.d("nickname-exists", "onResponse() of nickname NotExists called in MyinfoModifyFragment")
                    mBinding?.myinfoNicknameAlertMsgTv?.text = "사용할 수 있는 닉네임입니다"
                    mBinding?.myinfoNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.myinfoNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isModifyNickname = true
                    changeConfirmButton()
                }
                else {
                    Log.d("nickname-exists", "onResponse() of nickname Exists called in MyinfoModifyFragment")
                    mBinding?.myinfoNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                    mBinding?.myinfoNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                    mBinding?.myinfoNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isModifyNickname = false
                    changeConfirmButton()
                }
            },
            onErrorResponse = {
                // status 400 유효성 검사 실패
                if(it.details[0].code == "NotBlank" && it.details[0].field == "nickname") {
                    Log.d("nickname-exists", "onErrorResponse() called in MyinfoModifyFragment")
                    mBinding?.myinfoNicknameAlertMsgTv?.text = "사용할 수 없는 닉네임입니다"
                    mBinding?.myinfoNicknameAlertMsgTv?.setTextColor(resources.getColor(R.color.red))
                    mBinding?.myinfoNicknameAlertMsgTv?.visibility = View.VISIBLE
                    isModifyNickname = false
                    changeConfirmButton()
                }
            },
            onFailure = {
                Log.d("nickname-exists", "onFailure() called in MyinfoModifyFragment")
                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun changeConfirmButton() {
        if(isModifyNickname || isModifyIntroMsg) {
            mBinding?.myinfoConfirmBtn?.setTextColor(R.color.main_color)
            mBinding?.myinfoConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.myinfoConfirmBtn?.setTextColor(R.color.gray)
            mBinding?.myinfoConfirmBtn?.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPEN_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                var currentImageUri = data?.data
                requireActivity().contentResolver.openInputStream(currentImageUri!!)?.use {
                    inputStream ->
                    val file = createFile(requireContext(), "temp", "jpg")
                    inputStreamToFile(inputStream, file)
                }

                try {
                    currentImageUri.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                currentImageUri
                            )
                            mBinding?.myinfomodifyAddpictureBtn?.setImageBitmap(bitmap)
                        }
                        else {
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                currentImageUri
                            )
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            mBinding?.myinfomodifyAddpictureBtn?.setImageBitmap(bitmap)
                        }
                    }
                } catch(e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(requireActivity(), "사진 선택 취소", Toast.LENGTH_LONG).show()
        }
    }

    fun createFile(context: Context, fileName: String?, extension: String?): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File(storageDir, "$fileName.$extension")
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
            }
        }
    }
}