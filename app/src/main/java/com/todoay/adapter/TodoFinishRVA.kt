package com.todoay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.data.TodoData
import com.todoay.data.TodoFinishData
import com.todoay.databinding.ItemTodoPlanBinding
import com.todoay.databinding.ItemTodoPlanFinishBinding

class TodoFinishRVA: RecyclerView.Adapter<TodoFinishRVA.ViewHolder>() {

    var dataList = mutableListOf<TodoFinishData>()

    inner class ViewHolder(private val binding: ItemTodoPlanFinishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData : TodoFinishData) {
            binding.todoMainContent.text = todoData.todo_Content
            binding.todoMainDate.text =  todoData.todo_Duedate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoPlanFinishBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}