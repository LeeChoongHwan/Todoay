package com.todoay.view.todo.daily

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.api.domain.todo.common.TodoAPI
import com.todoay.api.domain.todo.daily.DailyTodoAPI
import com.todoay.data.category.Category
import com.todoay.data.todo.daily.Alarm
import com.todoay.data.todo.daily.DailyInfo
import com.todoay.databinding.FragmentDailyTodoInfoBinding
import com.todoay.global.util.PrintUtil
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.ModifiedTodoResult
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult

class DailyTodoInfoFragment(private var dailyInfo: DailyInfo) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDailyTodoInfoBinding

    lateinit var result : TodoInfoChangedStateResult

    private val commonService by lazy { TodoAPI.getInstance() }
    private val dailyService by lazy { DailyTodoAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDailyTodoInfoBinding.inflate(inflater, container, false)

        /* 수정하기 버튼 */
        binding.dailyTodoInfoModifyBtn.setOnClickListener {
            val modifyDailyTodoFragment = AddDailyTodoFragment(dailyInfo.date, dailyInfo.category).apply {
                this.isModificationMode = true
                this.modifiedData = dailyInfo
            }
            modifyDailyTodoFragment.show(parentFragmentManager, modifyDailyTodoFragment.tag)
            modifyDailyTodoFragment.modifiedResult = object : ModifiedTodoResult {
                override fun isModified(isModified: Boolean, id: Long) {
                    // TODO
                    result.isChangedState(true)
                }
            }
        }

        /* 삭제하기 버튼 */
        binding.dailyTodoInfoDeleteBtn.setOnClickListener {
            val alertDeleteDialog = TodoayAlertDialogFragment().apply {
                this.message = "정말 삭제하시겠어요?"
            }
            alertDeleteDialog.show(parentFragmentManager, alertDeleteDialog.tag)
            alertDeleteDialog.result = object : TodoayAlertDialogFragment.AlertDialogResult {
                override fun getValue(isPositive: Boolean) {
                    if(isPositive) {
                        commonService.deleteTodo(
                            dailyInfo.id,
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

        /* 반복하기 버튼 */
        binding.dailyTodoInfoRepeatBtn.setOnClickListener {

        }

        displayDailyInfo(dailyInfo)

        return binding.root
    }

    private fun displayDailyInfo(info : DailyInfo) {
        /* 공개여부 */
        if(info.isPublic) {
            binding.dailyTodoInfoPublicBtn.bringToFront()
        } else {
            binding.dailyTodoInfoPrivateBtn.bringToFront()
        }
        /* 카테고리 */
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_add_daily_todo_category_btn) as GradientDrawable
        drawable.setColor(Color.parseColor(info.category.color))
        binding.dailyTodoInfoCategoryBtn.setBackgroundDrawable(drawable)
        binding.dailyTodoInfoCategoryBtn.text = info.category.name
        /* 투두 이름 */
        binding.dailyTodoInfoTodoTitleTv.text = info.todo
        /* 해시태그 */
        if(!info.hashtagList.isNullOrEmpty()) {
            val hashtagSb = StringBuilder()
            info.hashtagList.stream()
                .map { h -> "#${h.name} "}
                .forEach(hashtagSb::append)
            binding.dailyTodoInfoHashtagTv.text = hashtagSb
        }
        else {
            binding.dailyTodoInfoHashtagTv.text = ""
        }
        /* 시간 및 알람 */
        if(info.time != null) {
            binding.dailyTodoInfoTimeTv.text = PrintUtil.convertDateTimePrintFormat(info.time)
            binding.dailyTodoInfoAlarmTv.text = info.alarm.toString()
        } else {
            hideView(binding.dailyTodoInfoTimeTitleTv, binding.dailyTodoInfoTimeTv)
            hideView(binding.dailyTodoInfoAlarmTitleTv, binding.dailyTodoInfoAlarmTv)
        }
        /* 장소 */
        if(!info.location.isNullOrEmpty()) {
            binding.dailyTodoInfoLocationTv.text = info.location
        } else {
            hideView(binding.dailyTodoInfoLocationTitleTv, binding.dailyTodoInfoLocationTv)
        }
        /* 함께하는 사람 */
        if(!info.partner.isNullOrEmpty()) {
            binding.dailyTodoInfoPartnerTv.text = info.partner
        } else {
            hideView(binding.dailyTodoInfoPartnerTitleTv, binding.dailyTodoInfoPartnerTv)
        }
    }

    private fun hideView(title: View, contents: View) {
        title.visibility = View.GONE
        contents.visibility = View.GONE
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