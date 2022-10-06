package com.todoay.view.setting.category

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.api.domain.category.CategoryAPI
import com.todoay.data.category.Category
import com.todoay.databinding.FragmentCategoryFinishInfoBinding
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.OnClickListener
import com.todoay.view.setting.category.interfaces.CategoryInfoResult

class CategoryFinishInfoFragment(private val category : Category) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentCategoryFinishInfoBinding

    lateinit var result : CategoryInfoResult

    private val service by lazy { CategoryAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryFinishInfoBinding.inflate(inflater, container, false)

        /* 카테고리 color */
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_category_color) as GradientDrawable
        drawable.setColor(Color.parseColor(category.color))
        binding.categoryFinishInfoColor.setImageDrawable(drawable)

        /* 카테고리 name */
        binding.categoryFinishInfoName.text = category.name

        /* 삭제 버튼 */
        binding.categoryFinishInfoDeleteBtn.setOnClickListener {
            TodoayAlertDialogFragment().apply {
                this.message = "카테고리를 삭제하면\n관련 TODO 모두 삭제됩니다.\n정말 삭제하시겠어요?"
                this.onClickListener = object : OnClickListener {
                    override fun onClick(item: Any) {
                        if (item as Boolean) {
                            service.deleteCategory(
                                category.id,
                                onResponse = {
                                    result.isChangedState(true)
                                    this@CategoryFinishInfoFragment.dismiss()
                                },
                                onErrorResponse = {
                                    mainAct!!.showShortToast("카테고리 삭제가 실패하였습니다.\n다시 시도해주세요")
                                },
                                onFailure = {}
                            )
                        }
                    }
                }
                this.show(this@CategoryFinishInfoFragment.parentFragmentManager, this.tag)
            }
        }

        return binding.root
    }

}