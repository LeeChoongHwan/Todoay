package com.example.bottomfragmenttest

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.databinding.FragmentTimeExistDialogBinding

class TimeExistDialogFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentTimeExistDialogBinding

    enum class TimeExistClick() {
        MODIFY,
        DELETE
    }

    lateinit var result : TimeExistDialogResult
    interface TimeExistDialogResult {
        fun getClick(click: TimeExistClick)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTimeExistDialogBinding.inflate(inflater, container, false)

        binding.timeExistDialogModifyBtn.setOnClickListener {
            dismissNow()
            result.getClick(TimeExistClick.MODIFY)
        }

        binding.timeExistDialogDeleteBtn.setOnClickListener {
            dismissNow()
            result.getClick(TimeExistClick.DELETE)
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
        다이얼로그 radius 및 height 지정
         */
        val resources = resources
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert(view != null)
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(70, 0, 70, 0)
            parent.layoutParams = layoutParams
        }
    }

}