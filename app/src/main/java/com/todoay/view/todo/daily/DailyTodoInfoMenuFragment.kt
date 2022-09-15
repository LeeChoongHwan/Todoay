package com.todoay.view.todo.daily

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.DailyRepeatSettingDialog
import com.todoay.data.todo.daily.DailyInfo
import com.todoay.databinding.FragmentDailyTodoInfoMenuBinding
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult

class DailyTodoInfoMenuFragment(private var dailyInfo: DailyInfo) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDailyTodoInfoMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDailyTodoInfoMenuBinding.inflate(inflater, container, false)

        displayDailyInfo(dailyInfo)

        /* 자세히 보기 버튼 */
        binding.dailyTodoInfoMenuDetailsBtn.setOnClickListener {
            val infoDialog = DailyTodoInfoFragment(dailyInfo)
            infoDialog.show(parentFragmentManager, infoDialog.tag)
            infoDialog.result = object : TodoInfoChangedStateResult {
                override fun isChangedState(isChanged: Boolean) {
                    if (isChanged) {
//                        getDailyTodoData()
                    }
                }
            }
        }

        /* 삭제하기 버튼 */
        binding.dailyTodoInfoMenuDeleteBtn.setOnClickListener {

        }

        /* 반복하기 버튼 */
        binding.dailyTodoInfoMenuRepeatBtn.setOnClickListener {
            val repeatSettingDialog = DailyRepeatSettingDialog()
            repeatSettingDialog.show(parentFragmentManager, repeatSettingDialog.tag)
        }

        /* 내일하기 버튼 */
        binding.dailyTodoInfoMenuDelayTomorrowBtn.setOnClickListener {

        }

        /* 날짜 바꾸기 버튼 */
        binding.dailyTodoInfoMenuChangeDateBtn.setOnClickListener {

        }

        return binding.root
    }

    private fun displayDailyInfo(info : DailyInfo) {
        /* 공개여부 */
        if(info.isPublic) {
            binding.dailyTodoInfoMenuPublicBtn.bringToFront()
        } else {
            binding.dailyTodoInfoMenuPrivateBtn.bringToFront()
        }
        /* 투두 이름 */
        binding.dailyTodoInfoMenuTodoTitleTv.text = info.todo
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* 다이얼로그 radius 및 height 지정 */
        val resources = resources
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert(view != null)
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(40, 0, 40, 0)
            parent.layoutParams = layoutParams
        }
    }
}