package com.todoay.view.todo.daily

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.data.todo.daily.Alarm
import com.todoay.databinding.DialogListItemBinding
import com.todoay.databinding.FragmentAlarmSettingDialogBinding
import com.todoay.global.util.PrintUtil
import com.todoay.global.util.PrintUtil.printLog
import java.time.LocalDateTime

class AlarmSettingDialog(/** 현재 설정된 투두의 시간 */val time: LocalDateTime) : DialogFragment() {


    lateinit var binding: FragmentAlarmSettingDialogBinding
    lateinit var selectedAlarm: LocalDateTime
    lateinit var result : AlarmSettingDialogResult
    var currentAlarm : Alarm? = null

    interface AlarmSettingDialogResult {
        fun getAlarm(alarm : Alarm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlarmSettingDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        if(currentAlarm != null) {
            binding.alarmSettingDialogCurrentAlarmTv.visibility = View.VISIBLE
            val currentAlarmNameText : String =
                (if(currentAlarm!!.alarmType != null) {
                    if(currentAlarm!!.alarmType == Alarm.AlarmType.CUSTOM) {
                        PrintUtil.convertDateTimePrintFormat(currentAlarm!!.alarmTime!!)
                    } else {
                        currentAlarm!!.alarmType!!.typeName
                    }
                } else {
                    currentAlarm = Alarm(time, currentAlarm!!.alarmTime!!)
                    currentAlarm!!.alarmType!!.typeName
                })
            binding.alarmSettingDialogCurrentAlarmTv.text = currentAlarmNameText
        }

        val alarms = ArrayList<String>()
        for (alarm in Alarm.AlarmType.values()) {
            alarms.add(alarm.typeName)
        }

        binding.alarmSettingDialogList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AlarmListAdapter(alarms)
        adapter.onItemClickListener = object : AlarmListAdapter.OnItemClickListener {
            override fun onClick(selectedAlarm: String) {
                when(Alarm.AlarmType.valueOfAlarm(selectedAlarm)) {
                    Alarm.AlarmType.FIVE_MINUTE -> {
                        checkIsBeforeTime(time.minusMinutes(5), Alarm.AlarmType.FIVE_MINUTE)
                    }
                    Alarm.AlarmType.TEN_MINUTE -> {
                        checkIsBeforeTime(time.minusMinutes(10), Alarm.AlarmType.TEN_MINUTE)
                    }
                    Alarm.AlarmType.THIRTY_MINUTE -> {
                        checkIsBeforeTime(time.minusMinutes(30), Alarm.AlarmType.THIRTY_MINUTE)
                    }
                    Alarm.AlarmType.ONE_HOUR -> {
                        checkIsBeforeTime(time.minusHours(1), Alarm.AlarmType.ONE_HOUR)
                    }
                    Alarm.AlarmType.CUSTOM -> {
                        val timePickerDialog = TimePickerDialogFragment(time.toLocalDate())
                        timePickerDialog.title = "커스텀 알람 설정"
                        timePickerDialog.limitTime = time
                        timePickerDialog.show(parentFragmentManager, timePickerDialog.tag)
                        dismissNow()
                        timePickerDialog.result = object : TimePickerDialogFragment.TimePickerDialogResult {
                            override fun getTime(time: LocalDateTime) {
                                this@AlarmSettingDialog.selectedAlarm = time
                                result.getAlarm(Alarm(this@AlarmSettingDialog.time, this@AlarmSettingDialog.selectedAlarm, Alarm.AlarmType.CUSTOM))
                            }
                        }
                    }
                    else -> {
                        dismissNow()
                        mainAct!!.showShortToast("알람 설정 오류가 발생하였습니다")
                        printLog("[ALARM SETTING ERROR] Could Not Found AlarmTime Instance")
                    }
                }
            }

        }
        binding.alarmSettingDialogList.adapter = adapter

        return binding.root
    }

    fun checkIsBeforeTime(selectedAlarm : LocalDateTime, alarmType: Alarm.AlarmType) {
        if(selectedAlarm.isAfter(time)) {
            mainAct!!.showShortToast("설정한 시간 이후에 알람을 설정할 수 없습니다!")
        } else if(selectedAlarm.isBefore(LocalDateTime.now())) {
            mainAct!!.showShortToast("현재 시간 이전에 알람을 설정할 수 없습니다!")
        }
        else {
            this.selectedAlarm = selectedAlarm
            result.getAlarm(Alarm(this.time, this.selectedAlarm, alarmType))
            dismissNow()
        }
    }

    class AlarmListAdapter(var alarmList: ArrayList<String>) : RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {

        lateinit var onItemClickListener: OnItemClickListener
        interface OnItemClickListener {
            fun onClick(selectedAlarm: String)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = DialogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view.root.layoutParams.apply {
                this.width = ViewGroup.LayoutParams.MATCH_PARENT
                this.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.alarmText.text = alarmList[position]
            holder.alarmText.setOnClickListener {
                onItemClickListener.onClick(alarmList[position])
            }
        }

        override fun getItemCount(): Int {
            return alarmList.size
        }

        class ViewHolder(val binding: DialogListItemBinding) : RecyclerView.ViewHolder(binding.root) {
            var alarmText = binding.alarmItem
        }

    }

}