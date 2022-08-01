package com.todoay.view.profile

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.api.domain.auth.nickname.NicknameAPI
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.FragmentProfileModifyBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern

class ProfileModifyFragment : Fragment() {

    private var mBinding : FragmentProfileModifyBinding?= null

    var isModifyImageUrl : Boolean = false
    var isModifyNickname : Boolean = false
    var isModifyIntroMsg : Boolean = false

    var currentImageUrl: String = ""
    var currentNickname: String = ""
    var currentIntroMsg: String = ""

    private var OPEN_GALLERY = 1

    private val profileService: ProfileAPI = ProfileAPI()
    private val nicknameService : NicknameAPI = NicknameAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileService.getProfile(
            onResponse = {
                currentNickname = it.nickname
                mBinding?.profileModifyNicknameEt?.setText(currentNickname)
                // 프로필 사진 세팅
                if(it.imageUrl!=null) {
                    currentImageUrl = it.imageUrl

                }
                // 상태메시지 세팅
                if(it.introMsg!=null) {
                    currentIntroMsg = it.introMsg
                    mBinding?.profileModifyMessageEt?.setText(currentIntroMsg)
                }
            },
            onErrorResponse = {
                // status 401 JWT 토큰 에러
                Toast.makeText(requireContext(), "로그인을 다시 해주세요", Toast.LENGTH_LONG).show()
            },
            onFailure = {
                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProfileModifyBinding.inflate(inflater,container,false)

        mBinding = binding

        /**
         * 닉네임 필드 변경
         */
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
                            mBinding?.profileModifyNicknameAlertMsgTv?.setTextColor(resources.getColor(
                                R.color.red
                            ))
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

        /**
         * 뒤로가기 버튼
         */
        mBinding?.profileModifyBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_profileModifyFragment_to_profileFragment)
        }

        /**
         * 확인 버튼
         */
        mBinding?.profileModifyConfirmBtn?.setOnClickListener {
            val inputImageUrl = ""
            val inputNickname = mBinding?.profileModifyNicknameEt?.text.toString()
            val inputIntroMsg = mBinding?.profileModifyMessageEt?.text.toString()
            // 프로필 수정 API 호출
            profileService.putProfile(
                inputNickname,
                inputIntroMsg,
                inputImageUrl,
                onResponse = {
                    Navigation.findNavController(view!!).navigate(R.id.action_profileModifyFragment_to_profileFragment)
                },
                onErrorResponse = {
                    // Status 400 유효성 검사 실패
                    // Status 401 JWT 토큰 에러

                },
                onFailure = {
                    Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
                }
            )
        }

        //사진
        mBinding?.profileModifyAddpictureBtn?.setOnClickListener(object : View.OnClickListener {
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
                Toast.makeText(requireContext(), it.code, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun changeConfirmButton() {
        if(isModifyNickname || isModifyIntroMsg) {
            mBinding?.profileModifyConfirmBtn?.setTextColor(R.color.main_color)
            mBinding?.profileModifyConfirmBtn?.isEnabled = true
        }
        else {
            mBinding?.profileModifyConfirmBtn?.setTextColor(R.color.gray)
            mBinding?.profileModifyConfirmBtn?.isEnabled = false
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
                            mBinding?.profileModifyAddpictureBtn?.setImageBitmap(bitmap)
                        }
                        else {
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                currentImageUri
                            )
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            mBinding?.profileModifyAddpictureBtn?.setImageBitmap(bitmap)
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