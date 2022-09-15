package com.todoay.view.todo.daily

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.api.domain.category.CategoryAPI
import com.todoay.data.category.Category
import com.todoay.databinding.FragmentAddDailyTodoCategorySettingDialogBinding
import com.todoay.databinding.ListItemDailyTodoCategorySettingBinding
import com.todoay.view.todo.daily.interfaces.AddDailyTodoCategorySettingDialogResult
import java.util.stream.Collectors


class AddDailyTodoCategorySettingDialog : DialogFragment() {

    lateinit var binding : FragmentAddDailyTodoCategorySettingDialogBinding

    lateinit var result : AddDailyTodoCategorySettingDialogResult

    private val service by lazy { CategoryAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentAddDailyTodoCategorySettingDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.addDailyTodoCategorySettingDialogList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CategorySettingAdapter(requireContext())
        adapter.onItemClickListener = object : CategorySettingAdapter.OnItemClickListener {
            override fun onClick(category: Category) {
                result.getCategory(category)
                dismissNow()
            }
        }

        service.readCategory(
            onResponse = {
                val categoryList = it.categoryList.stream()
                    .filter { c -> !c.isEnded }
                    .map { c -> Category(c.id, c.name, c.color, c.orderIndex, c.isEnded) }
                    .sorted(Comparator.comparing(Category::orderIndex))
                    .collect(Collectors.toList())
                adapter.categoryList = categoryList
                adapter.notifyDataSetChanged()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )

        binding.addDailyTodoCategorySettingDialogList.adapter = adapter

        return binding.root
    }


    class CategorySettingAdapter(val context: Context) : RecyclerView.Adapter<CategorySettingAdapter.ViewHolder>() {

        var categoryList : List<Category> = listOf()

        lateinit var onItemClickListener: OnItemClickListener
        interface OnItemClickListener {
            fun onClick(category: Category)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ListItemDailyTodoCategorySettingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.bg_category_color) as GradientDrawable
            drawable.setColor(Color.parseColor(categoryList[position].color))
            holder.color.setImageDrawable(drawable)
            holder.name.text = categoryList[position].name

            holder.layout.setOnClickListener {
                onItemClickListener.onClick(categoryList[position])
            }

        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        class ViewHolder(binding : ListItemDailyTodoCategorySettingBinding) : RecyclerView.ViewHolder(binding.root) {
            var layout = binding.dailyTodoCategorySettingLayout
            var color = binding.categoryColor
            var name = binding.categoryName
        }

    }

}