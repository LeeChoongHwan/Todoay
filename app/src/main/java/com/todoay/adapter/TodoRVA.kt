package com.todoay.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.data.TodoData
import com.todoay.databinding.ItemCalendarPlanBinding
import com.todoay.databinding.ItemTodoPlanBinding


class TodoRVA: RecyclerView.Adapter<TodoRVA.ViewHolder>() {

    var dataList = mutableListOf<TodoData>()

    inner class ViewHolder(private val binding: ItemTodoPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData : TodoData) {
            binding.todoMainDday.text = todoData.todo_Dday
            binding.todoMainUrgent.text = todoData.todo_Conditon
            binding.todoMainContent.text = todoData.todo_Content
            binding.todoMainDate.text =  todoData.todo_Duedate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoPlanBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size




}
