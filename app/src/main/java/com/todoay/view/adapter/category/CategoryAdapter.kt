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
import com.todoay.databinding.ListItemCategoryBinding
import com.todoay.view.setting.category.interfaces.CategoryOnClickListener
import com.todoay.view.setting.category.interfaces.ItemTouchHelperListener
import java.util.*

class CategoryAdapter(var context: Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), ItemTouchHelperListener {

    var dataList: ArrayList<Category> = ArrayList()

    lateinit var onClickListener: CategoryOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false))
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

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        dataList[fromPosition].orderIndex = toPosition
        dataList[toPosition].orderIndex = fromPosition
        Collections.swap(dataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        onClickListener.isChangedOrderIndex(true)
        return true
    }

    override fun onItemSwipe(position: Int) {
        // do nothing
    }

    class ViewHolder(val binding : ListItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        var layout = binding.categoryLayout
        var color : ImageView = binding.categoryColor
        var name : TextView = binding.categoryName
    }


}