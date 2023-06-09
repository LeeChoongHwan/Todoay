package com.todoay.view.global

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.todoay.databinding.FragmentTodoayAlertDialogBinding
import com.todoay.view.global.interfaces.OnClickListener

class TodoayAlertDialogFragment : DialogFragment(){

    lateinit var binding : FragmentTodoayAlertDialogBinding

    var message : String? = null

    lateinit var onClickListener: OnClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoayAlertDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        if(message == null)
            throw Exception("Alert Dialog에 Message가 없습니다!")

        binding.todoayAlertDialogAlertMsgTv.text = message!!

        binding.todoayAlertDialogPositiveBtn.setOnClickListener {
            onClickListener.onClick(true)
            dismiss()
        }

        binding.todoayAlertDialogNegativeBtn.setOnClickListener {
            onClickListener.onClick(false)
            dismiss()
        }

        return binding.root
    }

}