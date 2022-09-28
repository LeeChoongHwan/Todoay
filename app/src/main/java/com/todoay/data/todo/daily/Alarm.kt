package com.todoay.data.todo.daily

import com.todoay.global.util.PrintUtil
import com.todoay.global.util.PrintUtil.printLog
import java.time.Duration
import java.time.LocalDateTime

class Alarm() {

    lateinit var alarmTime : LocalDateTime
    lateinit var setTime : LocalDateTime
    var alarmType : AlarmType? = null

    constructor(setTime: LocalDateTime?, alarmTime : LocalDateTime?, alarmType: AlarmType) : this() {
        if (setTime != null && alarmTime != null) {
            this.setTime = setTime
            this.alarmTime = alarmTime
            this.alarmType = alarmType
        }
    }

    constructor(setTime : LocalDateTime?, alarmTime: LocalDateTime?) : this() {
        if (setTime != null && alarmTime != null) {
            this.setTime = setTime
            this.alarmTime = alarmTime
            this.alarmType = setTypeFromTime(setTime)
        }
    }

    enum class AlarmType(val typeName : String) {
        FIVE_MINUTE("5분 전"),
        TEN_MINUTE("10분 전"),
        THIRTY_MINUTE("30분 전"),
        ONE_HOUR("1시간 전"),
        CUSTOM("커스텀");
        companion object {
            fun valueOfAlarm(value: String): AlarmType? {
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

    override fun toString(): String {
        return when(this.alarmType) {
            AlarmType.FIVE_MINUTE -> "5분 전"
            AlarmType.TEN_MINUTE -> "10분 전"
            AlarmType.THIRTY_MINUTE -> "30분 전"
            AlarmType.ONE_HOUR -> "1시간 전"
            AlarmType.CUSTOM -> PrintUtil.convertDateTimePrintFormat(this.alarmTime)
            else -> ""
        }
    }

    private fun setTypeFromTime(time : LocalDateTime): AlarmType {
        val duration = Duration.between(this.alarmTime, time)
        return when {
            duration.toMinutes() == 5L -> AlarmType.FIVE_MINUTE
            duration.toMinutes() == 10L -> AlarmType.TEN_MINUTE
            duration.toMinutes() == 30L -> AlarmType.THIRTY_MINUTE
            duration.toHours() == 1L -> AlarmType.ONE_HOUR
            else -> AlarmType.CUSTOM
        }
    }
}
