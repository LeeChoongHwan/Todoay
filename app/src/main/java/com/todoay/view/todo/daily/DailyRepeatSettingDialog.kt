package com.todoay.view.todo.daily

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.api.domain.todo.daily.DailyTodoAPI
import com.todoay.api.domain.todo.daily.dto.request.DailyRepeatRequest
import com.todoay.data.todo.daily.DailyRepeat
import com.todoay.databinding.FragmentDailyRepeatSettingDialogBinding
import com.todoay.global.util.PrintUtil.printLog
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult


class DailyRepeatSettingDialog(private val dailyId : Long) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDailyRepeatSettingDialogBinding

    private var repeatType : DailyRepeat.RepeatType = DailyRepeat.RepeatType.DAILY
    private var repeatDuration : DailyRepeat.RepeatDuration = DailyRepeat.RepeatDuration.ONE_MONTH
    private var repeat : Int = 0

    private var isRepeatDurationCustom : Boolean = false

    private val durationOneMonth = "한달"
    private val durationNoEndDate = "무기한"
    private val durationCustom = "커스텀"

    private val customWeeks = "주"
    private val customMonths = "개월"
    private val customNumber = "회"

    private lateinit var slideDown : Animation
    private lateinit var slideUp : Animation

    lateinit var result : TodoInfoChangedStateResult

    private val service by lazy { DailyTodoAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDailyRepeatSettingDialogBinding.inflate(inflater, container, false)

        slideDown = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
        slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)

        /* 반복주기 */
        repeatType = DailyRepeat.RepeatType.DAILY // Default Type
        binding.dailyRepeatSettingTypeRadioGroup.setOnCheckedChangeListener { _, id ->
            when(id) {
                R.id.daily_repeat_setting_type_daily -> {
                    repeatType = DailyRepeat.RepeatType.DAILY
                    setDurationPicker()
                    if(isRepeatDurationCustom) {
                        setCustomRepeatPickerType()
                    }
                }
                R.id.daily_repeat_setting_type_weeks -> {
                    repeatType = DailyRepeat.RepeatType.WEEKS
                    setDurationPicker()
                    if(isRepeatDurationCustom) {
                        setCustomRepeatPickerType()
                    }
                }
                R.id.daily_repeat_setting_type_months -> {
                    repeatType = DailyRepeat.RepeatType.MONTHS
                    setDurationPicker()
                    if(isRepeatDurationCustom) {
                        setCustomRepeatPickerType()
                    }
                }
                R.id.daily_repeat_setting_type_years -> {
                    repeatType = DailyRepeat.RepeatType.YEARS
                    setDurationPicker()
                    if(isRepeatDurationCustom) {
                        setCustomRepeatPickerType()
                    }
                }
            }
        }

        /* 반복 기간 */
        binding.dailyRepeatSettingDurationBtn.setOnClickListener {
            binding.dailyRepeatSettingDurationPickerLayout.showAndHide()
            setDurationPicker()
        }

        /* 확인 버튼 */
        binding.dailyRepeatSettingConfirmBtn.setOnClickListener{
            val request = DailyRepeatRequest(
                repeatType = repeatType.type,
                duration = repeatDuration.duration,
                repeat = repeat
            )
            service.setDailyTodoRepeat(
                dailyId,
                request,
                onResponse = {
                    result.isChangedState(true)
                    dismiss()
                },
                onErrorResponse = {

                },
                onFailure = {}
            )
        }

        return binding.root
    }

    private fun View.showAndHide() {
        if(this.visibility == View.GONE) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    private fun setDurationPicker() {
        val durationList = when (repeatType) {
            DailyRepeat.RepeatType.DAILY, DailyRepeat.RepeatType.WEEKS -> listOf(
                durationOneMonth,
                durationNoEndDate,
                durationCustom
            )
            DailyRepeat.RepeatType.MONTHS, DailyRepeat.RepeatType.YEARS -> listOf(
                durationNoEndDate,
                durationCustom
            )
        }
        val durationPicker = binding.dailyRepeatSettingDurationPicker.apply {
            this.displayedValues = null
            this.minValue = 0
            this.maxValue = durationList.size - 1
            this.displayedValues = durationList.toTypedArray()
            this.value = durationList.indexOf(getCurrentDuration())
            this.wrapSelectorWheel = true
        }
        setSelectedDuration(durationList[durationPicker.value])

        durationPicker.setOnValueChangedListener { _, _, newVal ->
            setSelectedDuration(durationList[newVal])
        }
    }

    private fun getCurrentDuration() : String =  binding.dailyRepeatSettingDurationBtn.text.toString()

    private fun setSelectedDuration(durationStr: String) {
        when (durationStr) {
            durationOneMonth -> {
                binding.dailyRepeatSettingDurationBtn.text = durationOneMonth
                repeatDuration = DailyRepeat.RepeatDuration.ONE_MONTH
                repeat = -1
                showCustomRepeatPickerOrNot(false)
            }
            durationNoEndDate -> {
                binding.dailyRepeatSettingDurationBtn.text = durationNoEndDate
                repeatDuration = DailyRepeat.RepeatDuration.NO_END_DATE
                repeat = -1
                showCustomRepeatPickerOrNot(false)
            }
            durationCustom -> {
                binding.dailyRepeatSettingDurationBtn.text = durationCustom
                repeat = 1
                showCustomRepeatPickerOrNot(true)
            }
        }
    }

    private fun showCustomRepeatPickerOrNot(isCustom : Boolean) = if(isCustom) {
        isRepeatDurationCustom = isCustom
        binding.dailyRepeatSettingRepeatLayout.visibility = View.VISIBLE
        binding.dailyRepeatSettingRepeatBtn.setOnClickListener {
            binding.dailyRepeatSettingRepeatPickerLayout.showAndHide()
        }

        /* 커스텀 반복 타입 Picker */
        /*
        1. 타입이 일 -> 주, 월, 횟수(==일)
        2. 타입이 주 -> 주, 월, 횟수(==주)
        3. 타입이 월 -> 월, 횟수(==월)
        4. 타입이 년 -> 횟수(==년)
         */
        setCustomRepeatPickerType()

        /* 커스텀 반복 넘버 Picker */
        val repeatPicker = binding.dailyRepeatSettingRepeatPicker.apply {
            this.minValue = 1
            this.maxValue = 999
            this.value = 0
            this.wrapSelectorWheel = true
        }
        repeatPicker.setOnValueChangedListener { _, _, newVal ->
            repeat = newVal
            changeRepeatButtonText(repeat)
        }
    } else {
        binding.dailyRepeatSettingRepeatLayout.visibility = View.GONE
        binding.dailyRepeatSettingRepeatPickerLayout.visibility = View.GONE
    }


    private fun setCustomRepeatPickerType(): NumberPicker {
        val customRepeatTypeList: List<String> = when (repeatType) {
            DailyRepeat.RepeatType.DAILY, DailyRepeat.RepeatType.WEEKS -> listOf(customWeeks, customMonths, customNumber)
            DailyRepeat.RepeatType.MONTHS -> listOf(customMonths, customNumber)
            DailyRepeat.RepeatType.YEARS -> listOf(customNumber)
        }
        val repeatTypePicker = binding.dailyRepeatSettingRepeatTypePicker.apply {
            this.displayedValues = null
            this.minValue = 0
            this.maxValue = customRepeatTypeList.size - 1
            this.displayedValues = customRepeatTypeList.toTypedArray()
            this.value = 0
            this.wrapSelectorWheel = true
        }
        setRepeatDurationOfCustom(customRepeatTypeList[repeatTypePicker.value])

        repeatTypePicker.setOnValueChangedListener { _, _, newVal ->
            setRepeatDurationOfCustom(customRepeatTypeList[newVal])
        }
        return repeatTypePicker
    }

    private fun setRepeatDurationOfCustom(customDurationTypeStr: String) {
        when(customDurationTypeStr) {
            customWeeks -> {
                repeatDuration = DailyRepeat.RepeatDuration.CUSTOM_WEEKS
                changeRepeatButtonText(repeat, customDurationTypeStr)
            }
            customMonths -> {
                repeatDuration = DailyRepeat.RepeatDuration.CUSTOM_MONTH
                changeRepeatButtonText(repeat, customDurationTypeStr)
            }
            customNumber -> {
                repeatDuration = DailyRepeat.RepeatDuration.CUSTOM_NUMBER
                changeRepeatButtonText(repeat, customDurationTypeStr)
            }
        }
    }

    private fun changeRepeatButtonText(number: Int) {
        changeRepeatButtonText(number, getCurrentCustomRepeatButtonText())
    }

    private fun getCurrentCustomRepeatButtonText(): String = when(repeatDuration) {
        DailyRepeat.RepeatDuration.CUSTOM_WEEKS -> customWeeks
        DailyRepeat.RepeatDuration.CUSTOM_MONTH -> customMonths
        DailyRepeat.RepeatDuration.CUSTOM_NUMBER -> customNumber
        else -> ""
    }


    private fun changeRepeatButtonText(number : Int, type : String) {
        binding.dailyRepeatSettingRepeatBtn.text = "$number$type"
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
            layoutParams.setMargins(40, 0, 40, 0)
            parent.layoutParams = layoutParams
        }
    }

}