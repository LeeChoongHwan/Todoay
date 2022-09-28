package com.todoay.view.adapter.todo.daily

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.data.category.Category
import com.todoay.data.todo.daily.Daily
import com.todoay.databinding.ListItemDailyTodoCategoryBinding
import com.todoay.view.todo.daily.interfaces.DailyOnClickListenerForGetCategory
import com.todoay.view.todo.daily.interfaces.DailyOnClickListenerForGetDaily

class DailyTodoCategoryRvAdapter(val context : Context): RecyclerView.Adapter<DailyTodoCategoryRvAdapter.ViewHolder>() {

    var dailyCategoryList : List<Category> = listOf()
    var dailyTodoHashMap : HashMap<Long, ArrayList<Daily>> = HashMap()

    lateinit var onClickListenerForAddButton: DailyOnClickListenerForGetCategory
    lateinit var onClickListenerForDotButton : DailyOnClickListenerForGetDaily
    lateinit var onClickListenerForCheckBox : DailyOnClickListenerForGetDaily

    inner class ViewHolder(private val binding: ListItemDailyTodoCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.dailyTodoCategory.text = category.name
            val drawable = ContextCompat.getDrawable(context, R.drawable.category_background) as GradientDrawable
            drawable.setColor(Color.parseColor(category.color))
            binding.dailyTodoCategory.setBackgroundDrawable(drawable)

            val dailyAdapter = DailyTodoRvAdapter().apply {
                this.onClickDotListener = this@DailyTodoCategoryRvAdapter.onClickListenerForDotButton
                this.onClickCheckListener = this@DailyTodoCategoryRvAdapter.onClickListenerForCheckBox
            }
            if(dailyTodoHashMap.containsKey(category.id)) {
                dailyAdapter.dataList = dailyTodoHashMap[category.id]!!
            }
            dailyAdapter.notifyDataSetChanged()
            binding.todoMainRecyclerview.adapter = dailyAdapter

            binding.dailyTodoCategoryAdd.setOnClickListener {
                onClickListenerForAddButton.onClick(category)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDailyTodoCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyCategoryList[position])
    }

    override fun getItemCount(): Int = dailyCategoryList.size
}