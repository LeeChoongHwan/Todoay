package com.todoay.view.todo.daily

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.api.domain.todo.common.TodoAPI
import com.todoay.api.domain.todo.daily.DailyTodoAPI
import com.todoay.api.domain.todo.daily.dto.request.ModifyDailyTodoDateRequest
import com.todoay.data.todo.daily.DailyInfo
import com.todoay.databinding.FragmentDailyTodoInfoMenuBinding
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.OnClickListener
import com.todoay.view.todo.common.DatePickerDialogFragment
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult
import com.todoay.view.todo.daily.interfaces.ModifiedDailyData
import java.time.LocalDate

class DailyTodoInfoMenuFragment(private var dailyInfo: DailyInfo) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDailyTodoInfoMenuBinding

    lateinit var result: TodoInfoChangedStateResult

    private val commonService by lazy { TodoAPI.getInstance() }
    private val dailyService by lazy { DailyTodoAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDailyTodoInfoMenuBinding.inflate(inflater, container, false)

        displayDailyInfo(dailyInfo)

        /* 자세히 보기 버튼 */
        binding.dailyTodoInfoMenuDetailsBtn.setOnClickListener {
            val infoDialog = DailyTodoInfoFragment(dailyInfo)
            infoDialog.show(parentFragmentManager, infoDialog.tag)
            infoDialog.modifiedResult = object : ModifiedDailyData {
                override fun isModified(isModified: Boolean, modifiedData: DailyInfo) {
                    if(isModified) {
                        result.isChangedState(true, false)
                        displayDailyInfo(modifiedData)
                    }
                }
            }
            infoDialog.deleteResult = object : TodoInfoChangedStateResult {
                override fun isChangedState(isModified: Boolean, isDeleted: Boolean) {
                    if (isDeleted) {
                        result.isChangedState(isModified, isDeleted)
                        infoDialog.dismiss()
                    }
                }
            }
        }

        /* 삭제하기 버튼 */
        binding.dailyTodoInfoMenuDeleteBtn.setOnClickListener {
            // 반복 설정 X
            if(dailyInfo.repeatId < 1) {
                TodoayAlertDialogFragment().apply {
                    this.message = "정말 삭제하시겠어요?"
                    this.onClickListener = object : OnClickListener {
                        override fun onClick(item: Any) {
                            if(item as Boolean) {
                                deleteDailyTodo()
                            }
                        }
                    }
                    this.show(this@DailyTodoInfoMenuFragment.parentFragmentManager, this.tag)
                }
            }
            // 반복 설정 O
            else {
                DeleteRepeatDailyAlertDialog().apply {
                    this.onClickListener = object : OnClickListener {
                        override fun onClick(item: Any) {
                            when(item as String) {
                                "selected" -> deleteDailyTodo()
                                "all" -> deleteRepeatDailyTodo()
                            }
                        }
                    }
                    this.show(this@DailyTodoInfoMenuFragment.parentFragmentManager, this.tag)
                }
            }
        }

        /* 반복하기 버튼 */
        binding.dailyTodoInfoMenuRepeatBtn.setOnClickListener {
            val repeatSettingDialog = DailyRepeatSettingDialog(dailyInfo.id)
            repeatSettingDialog.show(parentFragmentManager, repeatSettingDialog.tag)
            repeatSettingDialog.result = object : TodoInfoChangedStateResult {
                override fun isChangedState(isModified: Boolean, isDeleted: Boolean) {
                    if(isModified) {
                        result.isChangedState(true, false)
                    }
                }
            }
        }

        /* 내일하기 버튼 */
        binding.dailyTodoInfoMenuDelayTomorrowBtn.setOnClickListener {
            modifyDailyDate(dailyInfo.date.plusDays(1))
        }

        /* 날짜 바꾸기 버튼 */
        binding.dailyTodoInfoMenuChangeDateBtn.setOnClickListener {
            val datePickerFragment = DatePickerDialogFragment(LocalDate.now())
            datePickerFragment.show(parentFragmentManager, datePickerFragment.tag)
            datePickerFragment.result = object: DatePickerDialogFragment.DatePickerResult {
                override fun getDate(date: LocalDate) {
                    modifyDailyDate(date)
                }
            }
        }

        return binding.root
    }

    private fun deleteDailyTodo() {
        commonService.deleteTodo(
            dailyInfo.id,
            onResponse = {
                result.isChangedState(false, true)
                dismiss()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

    private fun deleteRepeatDailyTodo() {
        dailyService.deleteRepeatDailyTodo(
            dailyInfo.id,
            onResponse = {
                result.isChangedState(false, true)
                dismiss()
            },
            onErrorResponse = {},
            onFailure = {}
        )
    }


    private fun modifyDailyDate(modifiedDate : LocalDate) {
        val request = ModifyDailyTodoDateRequest(
            date = modifiedDate
        )
        dailyService.modifyDailyTodoDate(
            dailyInfo.id,
            request,
            onResponse = {
                result.isChangedState(true, false)
                dismiss()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
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