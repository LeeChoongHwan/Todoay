package com.todoay.view.todo.common

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.util.*

class DatePickerDialogFragment(val date: LocalDate) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var maxDate: LocalDate? = null

    lateinit var result : DatePickerResult
    interface DatePickerResult {
        fun getDate(date: LocalDate)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = DatePickerDialog(requireContext(), this, date.year, date.monthValue-1, date.dayOfMonth)

        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        if(maxDate != null) {
            val c = Calendar.getInstance()
            c.set(maxDate!!.year, maxDate!!.monthValue-1, maxDate!!.dayOfMonth)
            dialog.datePicker.maxDate = c.timeInMillis
        }

        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val date = LocalDate.of(year, month+1, day)
        result.getDate(date)
    }


}