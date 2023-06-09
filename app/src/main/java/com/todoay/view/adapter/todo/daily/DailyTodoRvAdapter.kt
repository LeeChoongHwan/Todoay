package com.todoay.view.adapter.todo.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.data.todo.daily.Daily
import com.todoay.databinding.ListItemDailyTodoBinding
import com.todoay.view.todo.daily.interfaces.DailyOnClickListenerForGetDaily

class DailyTodoRvAdapter : RecyclerView.Adapter<DailyTodoRvAdapter.ViewHolder>() {

    lateinit var onClickDotListener: DailyOnClickListenerForGetDaily
    lateinit var onClickCheckListener : DailyOnClickListenerForGetDaily

    var dataList : List<Daily> = listOf()

    inner class ViewHolder(private val binding: ListItemDailyTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(daily: Daily) {
            if(daily.isFinished) {
                binding.dailyTodoCheckbox.isChecked = true
            }

            binding.dailyTodoCheckbox.text = daily.todo

            if(!daily.hashtagList.isNullOrEmpty()) {
                val pHashtag = StringBuilder()
                daily.hashtagList!!.forEach {
                    pHashtag.append("#${it.name} ")
                }
                binding.dailyTodoHashtag.text = pHashtag
            }

            binding.dailyTodoCheckbox.setOnClickListener {
                onClickCheckListener.onClick(daily)
            }
            binding.dailyTodoDot.setOnClickListener {
                onClickDotListener.onClick(daily)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDailyTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

}