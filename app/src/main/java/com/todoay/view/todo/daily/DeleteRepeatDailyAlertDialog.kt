package com.todoay.view.todo.daily

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.todoay.databinding.FragmentDeleteRepeatDailyAlertDialogBinding
import com.todoay.view.global.interfaces.OnClickListener

class DeleteRepeatDailyAlertDialog : DialogFragment() {

    private lateinit var binding : FragmentDeleteRepeatDailyAlertDialogBinding

    lateinit var onClickListener: OnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteRepeatDailyAlertDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.deleteRepeatDailyAlertDialogDeleteSelectedBtn.setOnClickListener {
            onClickListener.onClick("selected")
            dismiss()
        }

        binding.deleteRepeatDailyAlertDialogDeleteAllBtn.setOnClickListener {
            onClickListener.onClick("all")
            dismiss()
        }

        binding.deleteRepeatDailyAlertDialogCancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        dialogFragmentResize(this@DeleteRepeatDailyAlertDialog, 0.8)
    }

    private fun dialogFragmentResize(dialogFragment: DialogFragment, width: Double) {
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params : ViewGroup.LayoutParams? = dialogFragment.dialog?.window?.attributes
        val x : Int = if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            (size.x * width).toInt()
        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            (rect.width() * width).toInt()
        }
        params?.width = x
        dialogFragment.dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

}