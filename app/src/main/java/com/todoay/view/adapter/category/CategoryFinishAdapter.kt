package com.todoay.view.adapter.category

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.data.category.Category
import com.todoay.databinding.ListItemCategoryFinishBinding
import com.todoay.view.setting.category.interfaces.CategoryOnClickListener

class CategoryFinishAdapter(val context : Context) : RecyclerView.Adapter<CategoryFinishAdapter.ViewHolder>() {

    var dataList: List<Category> = listOf()

    lateinit var onClickListener: CategoryOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemCategoryFinishBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.bg_category_color) as GradientDrawable
        drawable.setColor(Color.parseColor(dataList[position].color))
        holder.color.setImageDrawable(drawable)
        holder.name.text = dataList[position].name

        holder.layout.setOnClickListener {
            onClickListener.onClick(dataList[position])
        }
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(val binding : ListItemCategoryFinishBinding) : RecyclerView.ViewHolder(binding.root) {
        var layout = binding.categoryFinishLayout
        var color : ImageView = binding.categoryFinishColor
        var name : TextView = binding.categoryFinishName
    }

}