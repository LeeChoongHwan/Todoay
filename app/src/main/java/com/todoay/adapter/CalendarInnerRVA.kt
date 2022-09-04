package com.todoay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.data.CalendarData
import com.todoay.data.CalendarInnerData
import com.todoay.databinding.ItemCalendarPlanBinding
import com.todoay.databinding.ItemCalendarPlanInsideBinding

class CalendarInnerRVA : RecyclerView.Adapter<CalendarInnerRVA.ViewHolder>() {

    var dataList = mutableListOf<CalendarInnerData>()

    inner class ViewHolder(private val binding: ItemCalendarPlanInsideBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarInnerData: CalendarInnerData) {
            binding.itemCalendarCheckbox.text = calendarInnerData.todo
            binding.itemCalendarHashtag.text = calendarInnerData.hashtag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarPlanInsideBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

}