package com.example.bottomfragmenttest

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.databinding.FragmentCategoryColorSettingDialogBinding
import com.todoay.databinding.ListItemCategoryColorBinding

class CategoryColorSettingDialog : BottomSheetDialogFragment() {

    lateinit var binding : FragmentCategoryColorSettingDialogBinding
    var categoryColorList : ArrayList<CategoryColorItem> = ArrayList()
    var selectedColorCode : String = ""

    lateinit var result: CategoryColorResult
    interface CategoryColorResult {
        fun getColor(colorCode: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryColorSettingDialogBinding.inflate(inflater, container, false)

        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_main_color_btn, resources.getString(R.color.main_color)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_red_btn, resources.getString(R.color.category_red)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_orange_btn, resources.getString(R.color.category_orange)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_yellow_btn, resources.getString(R.color.category_yellow)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_green_btn, resources.getString(R.color.category_green)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_mint_btn, resources.getString(R.color.category_mint)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_light_blue_btn, resources.getString(R.color.category_light_blue)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_blue_btn, resources.getString(R.color.category_blue)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_indigo_btn, resources.getString(R.color.category_indigo)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_purple_btn, resources.getString(R.color.category_purple)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_pink_btn, resources.getString(R.color.category_pink)))
        categoryColorList.add(CategoryColorItem(R.drawable.bg_category_hot_pink_btn, resources.getString(R.color.category_hot_pink)))

        binding.categoryColorSettingColorList.layoutManager = GridLayoutManager(requireContext(), 6)
        val adapter = CategoryColorAdapter(requireContext(), categoryColorList)
        adapter.onItemClickListener = object: CategoryColorAdapter.OnItemClickListener {
            override fun onClick(selectedColorCode: String) {
                this@CategoryColorSettingDialog.selectedColorCode = selectedColorCode
            }
        }
        binding.categoryColorSettingColorList.adapter = adapter

        binding.categoryColorSettingColorConfirmBtn.setOnClickListener {
            result.getColor(selectedColorCode)
            dismissNow()
        }

        return binding.root
    }

    class CategoryColorAdapter(val context: Context, var categoryColorList: ArrayList<CategoryColorItem>) : RecyclerView.Adapter<CategoryColorAdapter.ViewHolder>() {

        lateinit var onItemClickListener: OnItemClickListener
        interface OnItemClickListener {
            fun onClick(selectedColorCode : String)
        }

        var selectedPosition = 0
        var selectedColorCode = ""

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category_color, null)
//            view.apply {
//                this.layoutParams = RecyclerView.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//            }
            return ViewHolder(ListItemCategoryColorBinding.inflate(LayoutInflater.from(context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.colorRadioButton.setBackgroundResource(categoryColorList[position].drawableId)
            holder.colorRadioButton.isChecked = position == selectedPosition
            selectedColorCode = categoryColorList[selectedPosition].colorCode
            onItemClickListener.onClick(selectedColorCode)

            holder.colorRadioButton.setOnClickListener {
                selectedPosition = position
                selectedColorCode = categoryColorList[selectedPosition].colorCode
                onItemClickListener.onClick(selectedColorCode)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int {
            return categoryColorList.size
        }

        class ViewHolder(val binding: ListItemCategoryColorBinding) : RecyclerView.ViewHolder(binding.root) {
            var colorRadioButton: RadioButton = binding.categorySettingColor
        }
    }

    class CategoryColorItem(val drawableId: Int, val colorCode: String)

}