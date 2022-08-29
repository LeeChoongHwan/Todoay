package com.todoay.view.todo

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.databinding.FragmentCategoryAddBinding
import com.todoay.view.todo.CategoryColorSettingDialog

class CategoryAddFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentCategoryAddBinding

    var category: String = ""
    var categoryColor : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryAddBinding.inflate(inflater, container, false)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_category_color) as GradientDrawable
        drawable.setColor(requireContext().resources.getColor(R.color.main_color))
        binding.categoryAddColorBtn.setImageDrawable(drawable)

        /* 디폴트 컬러 */
        categoryColor = resources.getString(R.color.main_color)

        binding.categoryAddColorBtn.setOnClickListener {
            val categoryColorSettingDialog = CategoryColorSettingDialog()
            categoryColorSettingDialog.show(parentFragmentManager, categoryColorSettingDialog.tag)
            categoryColorSettingDialog.result = object : CategoryColorSettingDialog.CategoryColorResult {
                override fun getColor(colorCode: String) {
                    categoryColor = colorCode
                    drawable.setColor(Color.parseColor(categoryColor))
                    binding.categoryAddColorBtn.setImageDrawable(drawable)
                }
            }
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
        다이얼로그 radius 및 height 지정
         */
        val resources = resources
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert(view != null)
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(40, 0, 40, 0)
            parent.layoutParams = layoutParams
        }
    }
}