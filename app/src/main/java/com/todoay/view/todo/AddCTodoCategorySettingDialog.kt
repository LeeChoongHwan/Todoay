package com.todoay.view.todo

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
import com.todoay.databinding.FragmentAddCTodoCategorySettingDialogBinding
import com.todoay.databinding.ListItemCTodoCategorySettingBinding


class AddCTodoCategorySettingDialog : DialogFragment() {

    lateinit var binding : FragmentAddCTodoCategorySettingDialogBinding


    lateinit var categoryList : ArrayList<CategorySettingItem>

    var selectCategory = HashMap<String, Int>()

    lateinit var result : AddCTodoCategorySettingDialogResult
    interface AddCTodoCategorySettingDialogResult {
        fun getCategory(category: CategorySettingItem)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentAddCTodoCategorySettingDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        /* test variable */
        var categoryNameList = arrayOf("일반", "운동", "공부", "내맘")
        var categoryColorList = arrayOf(
            resources.getString(R.color.main_color),
            resources.getString(R.color.category_blue),
            resources.getString(R.color.category_red),
            resources.getString(R.color.category_indigo)
        )

        categoryList = ArrayList()
        for(i in categoryNameList.indices) {
            categoryList.add(CategorySettingItem(categoryNameList[i], categoryColorList[i]))
        }

        binding.addCTodoCategorySettingDialogList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CategorySettingAdapter(requireContext(), categoryList)
        adapter.onItemClickListener = object : CategorySettingAdapter.OnItemClickListener {
            override fun onClick(category: CategorySettingItem) {
                result.getCategory(category)
                dismissNow()
            }

        }
        binding.addCTodoCategorySettingDialogList.adapter = adapter

        return binding.root
    }


    class CategorySettingAdapter(val context: Context, var categoryList: ArrayList<CategorySettingItem>) : RecyclerView.Adapter<CategorySettingAdapter.ViewHolder>() {

        lateinit var onItemClickListener: OnItemClickListener
        interface OnItemClickListener {
            fun onClick(category: CategorySettingItem)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ListItemCTodoCategorySettingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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

        class ViewHolder(binding : ListItemCTodoCategorySettingBinding) : RecyclerView.ViewHolder(binding.root) {
            var layout = binding.cTodoCategorySettingLayout
            var color = binding.categoryColor
            var name = binding.categoryName
        }

    }

    class CategorySettingItem(
        val name : String,
        val color : String
    )

}