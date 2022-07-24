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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.databinding.FragmentMyinfoBinding
import com.todoay.databinding.FragmentMyinfoModifyBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class MyinfoModifyFragment : Fragment() {

    private var mBinding : FragmentMyinfoModifyBinding?= null

    var isNickname : Boolean = false
    private var OPEN_GALLERY = 1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyinfoModifyBinding.inflate(inflater,container,false)

        mBinding = binding

        mBinding?.myinfomodifyNicknameEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(mBinding?.myinfomodifyNicknameEt?.text.toString() != "") {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = true
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.main_color)
                }
                else {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = false
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.gray)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(mBinding?.myinfomodifyNicknameEt?.text.toString() != "") {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = true
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.main_color)
                }
                else {
                    mBinding?.myinfomodifyNicknamecheckBtn?.isEnabled = false
                    mBinding?.myinfomodifyNicknamecheckBtn?.setBackgroundResource(R.drawable.checkrepbtn_fail_background)
                    mBinding?.myinfomodifyNicknamecheckBtn?.setTextColor(R.color.gray)
                }
            }

        })

        mBinding?.myinfomodifyNicknamecheckBtn?.setOnClickListener {
            mBinding?.myinfoNicknameErrormessage?.visibility = View.VISIBLE
            isNickname = true
            changeConfirmButton()
        }

        mBinding?.myinfomodifyBackbtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
        }

        mBinding?.myinfoConfirmBtn?.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_myinfoModifyFragment_to_myinfoFragment)
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

    private fun changeConfirmButton() {
        if(isNickname == true) {
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