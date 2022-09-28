package com.todoay.view.adapter.todo.dueDate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.data.todo.dueDate.DueDate
import com.todoay.databinding.ListItemDueDateTodoBinding
import com.todoay.view.todo.common.interfaces.TodoOnClickIdListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DueDateTodoRvAdapter: RecyclerView.Adapter<DueDateTodoRvAdapter.ViewHolder>() {

    lateinit var onClickListener: TodoOnClickIdListener

    var dataList : List<DueDate> = listOf()

    inner class ViewHolder(private val binding: ListItemDueDateTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData : DueDate) {
            /* title */
            binding.listItemDueDateTodoContent.text = todoData.todo
            /* date */
            binding.listItemDueDateTodoDate.text = todoData.dueDate.format(DateTimeFormatter.ofPattern("MM/dd"))
            /* d-day & urgent */
            val today = LocalDate.now()
            val dDay = StringBuilder("D")
            val period = today.until(todoData.dueDate, ChronoUnit.DAYS)
            binding.listItemDueDateTodoUrgent.visibility = View.INVISIBLE
            if(period == 0L) {
                dDay.append("-DAY")
                binding.listItemDueDateTodoUrgent.visibility = View.VISIBLE
                binding.listItemDueDateTodoUrgent.text = "긴급!"
            }
            else if(period > 0L) {
                dDay.append("-$period")
                if(period < 4L) { // D-3부터 표시 ("임박!")
                    binding.listItemDueDateTodoUrgent.visibility = View.VISIBLE
                    binding.listItemDueDateTodoUrgent.text = "임박!"
                }
            }
            else { dDay.append("+${period * (-1)}") }
            binding.listItemDueDateTodoDday.text = dDay
            /* priority */
            when(todoData.priority) {
                "HIGH" -> {
                    binding.listItemDueDateTodoBg.setImageResource(R.drawable.bg_due_date_todo_high)
                }
                "MIDDLE" -> {
                    binding.listItemDueDateTodoBg.setImageResource(R.drawable.bg_due_date_todo_middle)
                }
                "LOW" -> {
                    binding.listItemDueDateTodoBg.setImageResource(R.drawable.bg_due_date_todo_low)
                }
            }
            /* hashtag */
            if(!todoData.hashtagList.isNullOrEmpty()) {
                val hashtagSb = StringBuilder()
                todoData.hashtagList.stream()
                    .map { h -> "#${h.name} "}
                    .forEach(hashtagSb::append)
                binding.listItemDueDateTodoHashtag.text = hashtagSb
            }

            /* 클릭 이벤트 */
            binding.listItemDueDateTodoBg.setOnClickListener {
                onClickListener.onClick(todoData.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDueDateTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

}
