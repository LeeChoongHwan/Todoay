package com.todoay.view.todo.dueDate

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.api.domain.todo.common.TodoAPI
import com.todoay.api.domain.todo.dueDate.DueDateTodoAPI
import com.todoay.data.todo.dueDate.DueDateInfo
import com.todoay.databinding.FragmentDueDateFinishTodoInfoBinding
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.OnClickListener
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DueDateFinishTodoInfoFragment(private val dueDateId : Long) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDueDateFinishTodoInfoBinding
    lateinit var dueDateInfo : DueDateInfo

    lateinit var result : TodoInfoChangedStateResult

    private val commonService by lazy { TodoAPI.getInstance() }
    private val dueDateService by lazy { DueDateTodoAPI.getInstance() }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDueDateFinishTodoInfoBinding.inflate(inflater, container, false)
        
        /* 닫기(취소) 버튼 */
        binding.dueDateFinishTodoInfoCancelBtn.bringToFront()
        binding.dueDateFinishTodoInfoCancelBtn.setOnClickListener { 
            dismiss()
        }

        /* 삭제하기 버튼 */
        binding.dueDateFinishTodoInfoDeleteBtn.setOnClickListener {
            TodoayAlertDialogFragment().apply {
                this.message = "정말 삭제하시겠어요?"
                this.onClickListener = object : OnClickListener {
                    override fun onClick(item: Any) {
                        if(item as Boolean) {
                            commonService.deleteTodo(
                                dueDateId,
                                onResponse = {
                                    result.isChangedState(isModified = false, isDeleted = true)
                                    dismiss()
                                },
                                onErrorResponse = {

                                },
                                onFailure = {}
                            )
                        }
                    }
                }
                this.show(this@DueDateFinishTodoInfoFragment.parentFragmentManager, this.tag)
            }
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
            binding.dueDateFinishTodoInfoPublicBtn.bringToFront()
        } else {
            binding.dueDateFinishTodoInfoPrivateBtn.bringToFront()
        }
        /* 투두 이름 */
        binding.dueDateFinishTodoInfoTodo.text = info.todo
        /* Due-Date */
        binding.dueDateFinishTodoInfoDateTv.text =
            info.dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        /* 해시태그 */
        if(!info.hashtagList.isNullOrEmpty()) {
            val hashtagSb = StringBuilder()
            info.hashtagList.stream()
                .map { h -> "#${h.name} "}
                .forEach(hashtagSb::append)
            binding.dueDateFinishTodoInfoHashtagTv.text = hashtagSb
        }
        else {
            binding.dueDateFinishTodoInfoHashtagTv.text = ""
        }
        /* 설명 */
        binding.dueDateFinishTodoInfoDescriptionTv.text = info.description
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