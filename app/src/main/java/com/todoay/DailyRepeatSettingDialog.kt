package com.todoay

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.todoay.data.todo.daily.DailyRepeat
import com.todoay.databinding.FragmentAlarmSettingDialogBinding
import com.todoay.databinding.FragmentDailyRepeatSettingDialogBinding
import com.todoay.global.util.PrintUtil.printLog
import java.util.*

class DailyRepeatSettingDialog : DialogFragment() {

    lateinit var binding : FragmentDailyRepeatSettingDialogBinding

    private lateinit var repeatType : DailyRepeat.RepeatType
    private lateinit var repeatDuration : DailyRepeat.RepeatDuration
    private var repeat : Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDailyRepeatSettingDialogBinding.inflate(inflater, container, false)
        binding.dailyRepeatSettingDurationLayout.layoutParams.apply {
            this.width = MATCH_PARENT
            this.height = WRAP_CONTENT
        }

        /* 반복주기 */
        repeatType = DailyRepeat.RepeatType.DAILY // Default Type
        binding.dailyRepeatSettingTypeRadioGroup.setOnCheckedChangeListener { _, id ->
            when(id) {
                R.id.daily_repeat_setting_type_daily -> repeatType = DailyRepeat.RepeatType.DAILY
                R.id.daily_repeat_setting_type_weeks -> repeatType = DailyRepeat.RepeatType.WEEKS
                R.id.daily_repeat_setting_type_months -> repeatType = DailyRepeat.RepeatType.MONTHS
                R.id.daily_repeat_setting_type_years -> repeatType = DailyRepeat.RepeatType.YEARS
            }
        }

        /* 반복 기간 */
        val durationList = listOf("한달", "무기한", "커스텀")
        val durationPicker = binding.dailyRepeatSettingDurationPicker.apply {
            this.minValue = 0
            this.maxValue = durationList.size - 1
            this.displayedValues = durationList.toTypedArray()
            this.wrapSelectorWheel = true
        }
        // Default : 한달
        durationPicker.setOnValueChangedListener { _, oldValue, newValue ->
            printLog("oldValue : ${durationList[oldValue]} / newValue : ${durationList[newValue]}")
            when(durationList[newValue]) {
                "한달" -> {
                    repeatDuration = DailyRepeat.RepeatDuration.ONE_MONTH
                    showRepeatPickerOrNot(false)
                }
                "무기한" -> {
                    repeatDuration = DailyRepeat.RepeatDuration.NO_END_DATE
                    showRepeatPickerOrNot(false)
                }
                "커스텀" -> {
                    // TODO RepeatPicker 생성해야 함
                    showRepeatPickerOrNot(true)
                }
            }
        }

        /* 확인 버튼 */
        binding.dailyRepeatSettingConfirmBtn.setOnClickListener{

        }

        return binding.root
    }

    private fun showRepeatPickerOrNot(isCustom : Boolean) = if(isCustom) {
        binding.dailyRepeatSettingRepeatLayout.visibility = View.VISIBLE
        binding.dailyRepeatSettingRepeatPickerLayout.visibility = View.VISIBLE
        /* 커스텀 반복 숫자 Picker */
        val repeatPicker = binding.dailyRepeatSettingRepeatPicker.apply {
            this.minValue = 1
            this.maxValue = 999
            this.wrapSelectorWheel = true
        }
        repeatPicker.setOnValueChangedListener { _, oldValue, newValue ->
            printLog("oldValue : $oldValue / newValue : $newValue")
            repeat = newValue
        }
        /* 커스텀 반복 타입 Picker */
        /*
        1. 타입이 일 -> 주, 월, 횟수(==일)
        2. 타입이 주 -> 주, 월, 횟수(==주)
        3. 타입이 월 -> 월, 횟수(==월)
        4. 타입이 년 -> 횟수(==년)
         */
        val customRepeatTypeList : List<String> = when(repeatType) {
            DailyRepeat.RepeatType.DAILY -> {
                listOf("주", "월", "회")
            }
            DailyRepeat.RepeatType.WEEKS -> {
                listOf("주", "월", "회")
            }
            DailyRepeat.RepeatType.MONTHS -> {
                listOf("월", "회")
            }
            DailyRepeat.RepeatType.YEARS -> {
                listOf("회")
            }
        }
        val repeatTypePicker = binding.dailyRepeatSettingRepeatTypePicker.apply {
            this.minValue = 0
            this.maxValue = customRepeatTypeList.size - 1
            this.displayedValues = customRepeatTypeList.toTypedArray()
            this.wrapSelectorWheel = true
        }
        repeatTypePicker.setOnValueChangedListener { _, old, new ->
            printLog("select Type : ${customRepeatTypeList[new]}")
        }
    } else {
        binding.dailyRepeatSettingRepeatLayout.visibility = View.GONE
        binding.dailyRepeatSettingRepeatPickerLayout.visibility = View.GONE
    }

}