package com.todoay.view.adapter.todo.dueDate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.data.todo.dueDate.DueDate
import com.todoay.databinding.ListItemDueDateTodoFinishBinding
import java.time.format.DateTimeFormatter

class DueDateTodoFinishRvAdapter: RecyclerView.Adapter<DueDateTodoFinishRvAdapter.ViewHolder>() {

    lateinit var onClickListener: DueDateTodoFinishOnClickListener
    interface DueDateTodoFinishOnClickListener {
        fun onClick(id : Long)
    }

    var dataList : List<DueDate> = listOf()

    inner class ViewHolder(private val binding: ListItemDueDateTodoFinishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData : DueDate) {
            binding.listItemDueDateTodoFinishContent.text = todoData.todo
            binding.listItemDueDateTodoFinishDate.text =  todoData.dueDate.format(DateTimeFormatter.ofPattern("MM/dd"))

            binding.listItemDueDateTodoFinishBg.setOnClickListener {
                onClickListener.onClick(todoData.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDueDateTodoFinishBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}