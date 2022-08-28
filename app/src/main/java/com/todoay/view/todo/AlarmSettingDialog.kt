package com.example.bottomfragmenttest

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.databinding.DialogListItemBinding
import com.todoay.databinding.FragmentAlarmSettingDialogBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AlarmSettingDialog(val time: LocalDateTime) : DialogFragment() {
/* time 변수는 현재 설정된 투두의 시간을 가리킴 */

    lateinit var binding: FragmentAlarmSettingDialogBinding
    lateinit var selectedAlarm: LocalDateTime
    lateinit var result : AlarmSettingDialogResult
    var currentAlarmName : String? = null

    enum class AlarmTime(val alarmName : String) {
        FIVE_MINUTE("5분 전"),
        TEN_MINUTE("10분 전"),
        THIRTY_MINUTE("30분 전"),
        ONE_HOUR("1시간 전"),
        CUSTOM("커스텀");
        companion object {
            fun valueOfAlarm(value: String): AlarmTime? {
                return when(value) {
                    "5분 전" -> FIVE_MINUTE
                    "10분 전" -> TEN_MINUTE
                    "30분 전" -> THIRTY_MINUTE
                    "1시간 전" -> ONE_HOUR
                    "커스텀" -> CUSTOM
                    else -> null
                }
            }
        }
    }

    interface AlarmSettingDialogResult {
        fun getAlarm(_alarmTime: LocalDateTime, _alarmName: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlarmSettingDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        if(currentAlarmName != null) {
            binding.alarmSettingDialogCurrentAlarmTv.visibility = View.VISIBLE
            val currentAlarmNameText = binding.alarmSettingDialogCurrentAlarmTv.text.toString().plus(currentAlarmName)
            binding.alarmSettingDialogCurrentAlarmTv.text = currentAlarmNameText
        }

        val alarms = ArrayList<String>()
        for (alarm in AlarmTime.values()) {
            alarms.add(alarm.alarmName)
        }

        binding.alarmSettingDialogList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AlarmListAdapter(alarms)
        adapter.onItemClickListener = object : AlarmListAdapter.OnItemClickListener {
            override fun onClick(selectedAlarm: String) {
                when(AlarmTime.valueOfAlarm(selectedAlarm)) {
                    AlarmTime.FIVE_MINUTE -> {
                        checkIsBeforeTime(time.minusMinutes(5), AlarmTime.FIVE_MINUTE)
                    }
                    AlarmTime.TEN_MINUTE -> {
                        checkIsBeforeTime(time.minusMinutes(10), AlarmTime.TEN_MINUTE)
                    }
                    AlarmTime.THIRTY_MINUTE -> {
                        checkIsBeforeTime(time.minusMinutes(30), AlarmTime.THIRTY_MINUTE)
                    }
                    AlarmTime.ONE_HOUR -> {
                        checkIsBeforeTime(time.minusHours(1), AlarmTime.ONE_HOUR)
                    }
                    AlarmTime.CUSTOM -> {
                        val timePickerDialog = TimePickerDialogFragment(time.toLocalDate())
                        timePickerDialog.title = "커스텀 알람 설정"
                        timePickerDialog.limitTime = time
                        timePickerDialog.show(parentFragmentManager, timePickerDialog.tag)
                        dismissNow()
                        timePickerDialog.result = object : TimePickerDialogFragment.TimePickerDialogResult {
                            override fun getTime(time: LocalDateTime) {
                                this@AlarmSettingDialog.selectedAlarm = time
                                result.getAlarm(this@AlarmSettingDialog.selectedAlarm,
                                    DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH).format(this@AlarmSettingDialog.selectedAlarm)
                                )
                            }
                        }
                    }
                    else -> {
                        dismissNow()
                        Toast.makeText(requireContext(), "알람 설정 오류가 발생하였습니다", Toast.LENGTH_SHORT).show()
                        Log.d("ALARM SETTING ERROR", "Could Not Found AlarmTime Instance")
                    }
                }
            }

        }
        binding.alarmSettingDialogList.adapter = adapter

        return binding.root
    }

    fun checkIsBeforeTime(selectedAlarm : LocalDateTime, alarmType: AlarmTime) {
        if(selectedAlarm.isAfter(time)) {
            Toast.makeText(requireContext(), "설정한 시간 이후에 알람을 설정할 수 없습니다!", Toast.LENGTH_SHORT).show()
        } else if(selectedAlarm.isBefore(LocalDateTime.now())) {
            Toast.makeText(requireContext(), "현재 시간 이전에 알람을 설정할 수 없습니다!", Toast.LENGTH_SHORT).show()
        }
        else {
            this.selectedAlarm = selectedAlarm
            result.getAlarm(selectedAlarm, alarmType.alarmName)
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