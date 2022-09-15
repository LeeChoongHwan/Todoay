package com.todoay.view.todo.dueDate

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.api.domain.todo.common.TodoAPI
import com.todoay.api.domain.todo.dueDate.DueDateTodoAPI
import com.todoay.data.todo.dueDate.DueDateInfo
import com.todoay.databinding.FragmentDueDateTodoInfoBinding
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.ModifiedTodoResult
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DueDateTodoInfoFragment(private val dueDateId : Long) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDueDateTodoInfoBinding
    lateinit var dueDateInfo : DueDateInfo

    lateinit var result : TodoInfoChangedStateResult

    private val commonService by lazy { TodoAPI.getInstance() }
    private val dueDateService by lazy { DueDateTodoAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDueDateTodoInfoBinding.inflate(inflater, container, false)

        /* 닫기(취소) 버튼 */
        binding.dueDateTodoInfoCancelBtn.bringToFront()
        binding.dueDateTodoInfoCancelBtn.setOnClickListener {
            dismiss()
        }

        /* 수정하기 버튼 */
        // AddDueDateTodo 호출
        binding.dueDateTodoInfoModifyBtn.setOnClickListener {
            val modifyDueDateTodoFragment = AddDueDateTodoFragment().apply {
                this.isModificationMode = true
                this.modifiedData = dueDateInfo
            }
            modifyDueDateTodoFragment.show(parentFragmentManager, modifyDueDateTodoFragment.tag)
            modifyDueDateTodoFragment.modifiedResult = object : ModifiedTodoResult {
                override fun isModified(isResult: Boolean, id : Long) {
                    if(isResult) {
                        getDueDateInfo(id)
                        result.isChangedState(true)
                    }
                }
            }
        }

        /* 삭제하기 버튼 */
        binding.dueDateTodoInfoDeleteBtn.setOnClickListener {
            val alertDeleteDialog = TodoayAlertDialogFragment().apply {
                this.message = "정말 삭제하시겠어요?"
            }
            alertDeleteDialog.show(parentFragmentManager, alertDeleteDialog.tag)
            alertDeleteDialog.result = object : TodoayAlertDialogFragment.AlertDialogResult {
                override fun getValue(isPositive: Boolean) {
                    if(isPositive) {
                        commonService.deleteTodo(
                            dueDateId,
                            onResponse = {
                                result.isChangedState(true)
                                dismiss()
                            },
                            onErrorResponse = {

                            },
                            onFailure = {}
                        )
                    }
                }
            }
        }

        /* 완료하기 버튼 */
        binding.dueDateTodoInfoFinishBtn.setOnClickListener {
            commonService.switchTodoFinishState(
                dueDateId,
                onResponse = {
                    result.isChangedState(true)
                    dismiss()
                },
                onErrorResponse = {

                },
                onFailure = {}
            )

        }

        /* Info 가져오기 */
        getDueDateInfo(dueDateId)

        return binding.root
    }

    private fun getDueDateInfo(id : Long) {
        dueDateService.readDueDateTodoInfo(
            id,
            onResponse = { dto ->
                dueDateInfo = DueDateInfo(
                    id = dto.id,
                    todo = dto.todo,
                    description = dto.description,
                    hashtagList = dto.hashtagList,
                    dueDate = dto.dueDate,
                    priority = dto.priority,
                    isPublic = dto.isPublic,
                    isFinish = dto.isFinish
                )
                displayDueDateInfo(dueDateInfo)
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

    private fun displayDueDateInfo(info : DueDateInfo) {
        /* 공개여부 */
        if(info.isPublic) {
            binding.dueDateTodoInfoPublicBtn.bringToFront()
        } else {
            binding.dueDateTodoInfoPrivateBtn.bringToFront()
        }
        /* 투두 이름 */
        binding.dueDateTodoInfoTodo.text = info.todo
        /* Due-Date */
        binding.dueDateTodoInfoDateTv.text =
            info.dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        /* D-day */
        val today = LocalDate.now()
        val dDay = StringBuilder("D")
        val period = today.until(info.dueDate, ChronoUnit.DAYS).toInt()
        if(period == 0) {
            dDay.append("-DAY")
            binding.dueDateTodoInfoDdayTv.setTextColor(resources.getColor(R.color.red))
        }
        else if(period > 0) {
            dDay.append("-$period")
            if(period < 4) {
                binding.dueDateTodoInfoDdayTv.setTextColor(resources.getColor(R.color.red))
            }
        }
        else {
            dDay.append("+${period * (-1)}")
            binding.dueDateTodoInfoDdayTv.setTextColor(resources.getColor(R.color.gray))
        }
        binding.dueDateTodoInfoDdayTv.text = dDay
        /* 우선순위 */
        when(info.priority) {
            "HIGH" -> binding.dueDateTodoInfoTodo.setBackgroundResource(R.drawable.bg_due_date_todo_high)
            "MIDDLE" -> binding.dueDateTodoInfoTodo.setBackgroundResource(R.drawable.bg_due_date_todo_middle)
            "LOW" -> binding.dueDateTodoInfoTodo.setBackgroundResource(R.drawable.bg_due_date_todo_low)
        }
        /* 해시태그 */
        if(!info.hashtagList.isNullOrEmpty()) {
            val hashtagSb = StringBuilder()
            info.hashtagList.stream()
                .map { h -> "#${h.name} "}
                .forEach(hashtagSb::append)
            binding.dueDateTodoInfoHashtagTv.text = hashtagSb
        }
        else {
            binding.dueDateTodoInfoHashtagTv.text = ""
        }
        /* 설명 */
        binding.dueDateTodoInfoDescriptionTv.text = info.description
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