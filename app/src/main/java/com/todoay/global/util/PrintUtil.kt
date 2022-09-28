package com.todoay.global.util

import android.util.Log
import androidx.fragment.app.Fragment
import com.todoay.global.constants.StringConstants.TAG
import com.todoay.view.global.NetworkNotFoundFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object PrintUtil {

    /**
     * 전달받은 log 메시지를 담은 Log를 출력하는 메소드
     *
     * @param log Log에 담을 메시지
     */
    fun printLog(log: String) {
        Log.d(TAG, log)
    }

    fun convertDateTimePrintFormat(time : LocalDateTime) : String {
        return DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH).format(time)
    }
}