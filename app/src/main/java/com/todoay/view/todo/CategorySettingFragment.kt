package com.todoay.view.todo

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.databinding.FragmentCategorySettingBinding


class CategorySettingFragment : Fragment() {

    lateinit var binding : FragmentCategorySettingBinding
    var categoryList : ArrayList<CategoryItem> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategorySettingBinding.inflate(inflater, container, false)

        categoryList.add(CategoryItem(R.color.main_color, "일반"))
        categoryList.add(CategoryItem(R.color.light_blue, "운동"))
        categoryList.add(CategoryItem(R.color.red_bin, "취미"))
        categoryList.add(CategoryItem(R.color.gray, "공부"))

        binding.categorySettingList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CategoryAdapter(categoryList, requireContext())
        binding.categorySettingList.adapter = adapter

        binding.categorySettingPlusBtn.setOnClickListener {
            val addCategoryFragment = CategoryAddFragment()
            addCategoryFragment.show(parentFragmentManager, addCategoryFragment.tag)
        }

        return binding.root
    }

    class CategoryAdapter(var categoryList: ArrayList<CategoryItem>, var context: Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, null)
            view.apply {
                this.layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.bg_category_color) as GradientDrawable
            drawable.setColor(context.resources.getColor(categoryList[position].color))
            holder.color.setImageDrawable(drawable)
            holder.name.text = categoryList[position].name
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var color : ImageView
            var name : TextView
            init {
                color = itemView.findViewById(R.id.category_color)
                name = itemView.findViewById(R.id.category_name)
            }
        }

    }

    class CategoryItem(
        val color : Int,
        val name : String
    )


}