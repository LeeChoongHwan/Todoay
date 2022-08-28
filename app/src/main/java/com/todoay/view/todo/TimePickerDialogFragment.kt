package com.example.bottomfragmenttest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.databinding.FragmentTimePickerDialogBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimePickerDialogFragment(var date: LocalDate) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentTimePickerDialogBinding

    var title: String? = null
    var currentTime : String? = null
    /* limitTime 변수는 알람설정 시 설정된 시간을 최대 제한으로 두기 위한 변수. */
    var limitTime : LocalDateTime? = null
    lateinit var selectedTime : LocalDateTime

    lateinit var result : TimePickerDialogResult
    interface TimePickerDialogResult {
        fun getTime(time: LocalDateTime)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTimePickerDialogBinding.inflate(inflater, container, false)

        /* init */
        if(title != null) binding.timePickerDialogTitleTv.text = title
        if(currentTime != null) {
            binding.timePickerDialogCurrentTimeTv.visibility = View.VISIBLE
            val currentTimeText = binding.timePickerDialogCurrentTimeTv.text.toString().plus(currentTime)
            binding.timePickerDialogCurrentTimeTv.text = currentTimeText
        }
        if(limitTime == null) {
            val now = LocalTime.now()
            val currentHour = now.hour
            val currentMinute = now.minute
            binding.timePickerDialogTimePicker.hour = currentHour
            binding.timePickerDialogTimePicker.minute = currentMinute
        }
        /* 제한 시간이 있는 경우 (알람설정 시) */
        else {
            binding.timePickerDialogForAlarmCalendarBtn.visibility = View.VISIBLE
            binding.timePickerDialogForAlarmCurrentDateTv.text = LocalDate.from(limitTime).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            binding.timePickerDialogForAlarmCurrentDateTv.visibility = View.VISIBLE

            binding.timePickerDialogTimePicker.hour = limitTime!!.hour
            binding.timePickerDialogTimePicker.minute = limitTime!!.minute
        }

        /* 날짜 선택 버튼 (알람설정 시) */
        binding.timePickerDialogForAlarmCalendarBtn.setOnClickListener {
            val datePickerFragment = DatePickerDialogFragment(date)
            datePickerFragment.maxDate = LocalDate.from(limitTime)
            datePickerFragment.show(parentFragmentManager, datePickerFragment.tag)
            datePickerFragment.result = object: DatePickerDialogFragment.DatePickerResult {
                override fun getDate(date: LocalDate) {
                    this@TimePickerDialogFragment.date = date
                    val printDate = this@TimePickerDialogFragment.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                    binding.timePickerDialogForAlarmCurrentDateTv.text = printDate
                }
            }
        }

        /* 확인 버튼 */
        binding.timePickerDialogConfirmBtn.setOnClickListener {
            val timePicker = binding.timePickerDialogTimePicker
            val hour = timePicker.hour
            val minute = timePicker.minute
            this.selectedTime = LocalDateTime.of(date, LocalTime.of(hour, minute))
            if(this.selectedTime.isBefore(LocalDateTime.now())) {
                Toast.makeText(requireContext(), "현재시간보다 이전 시간을 설정할 수 없습니다!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(limitTime != null) {
                if(this.selectedTime.isAfter(limitTime)) {
                    Toast.makeText(requireContext(), "설정한 시간 이후에 알람을 설정할 수 없습니다!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            result.getTime(this.selectedTime)
            dismiss()
        }

        /* 취소 버튼 */
        binding.timePickerDialogCancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}