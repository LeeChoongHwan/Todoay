package com.todoay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.data.CalendarData
import com.todoay.databinding.ItemCalendarPlanBinding

class CalendarRVA : RecyclerView.Adapter<CalendarRVA.ViewHolder>(){

    var dataList = mutableListOf<CalendarData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarPlanBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: ItemCalendarPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(calenderData : CalendarData) {
            binding.calanderMainCategory.text
        }
    }
}

