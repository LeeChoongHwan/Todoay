package com.todoay.view.setting.category

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
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.api.domain.category.CategoryAPI
import com.todoay.data.category.Category
import com.todoay.databinding.FragmentCategoryInfoBinding
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.OnClickListener
import com.todoay.view.setting.category.interfaces.CategoryInfoResult
import com.todoay.view.setting.category.interfaces.ModifiedCategoryResult

class CategoryInfoFragment(private val category : Category) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentCategoryInfoBinding

    lateinit var result : CategoryInfoResult

    private val service by lazy { CategoryAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryInfoBinding.inflate(inflater, container, false)

        /* 카테고리 color */
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_category_color) as GradientDrawable
        drawable.setColor(Color.parseColor(category.color))
        binding.categoryInfoColor.setImageDrawable(drawable)

        /* 카테고리 name */
        binding.categoryInfoName.text = category.name

        /* 수정 버튼 */
        binding.categoryInfoModifyBtn.setOnClickListener {
            val modifyCategoryFragment = AddCategoryFragment(category.orderIndex).apply {
                this.isModificationMode = true
                this.modifiedCategory = category
            }
            modifyCategoryFragment.show(parentFragmentManager, modifyCategoryFragment.tag)
            modifyCategoryFragment.modifiedResult = object : ModifiedCategoryResult {
                override fun isModified(isResult: Boolean, modifiedCategory : Category) {
                    if(isResult) {
                        notifyCategoryDataChanged(modifiedCategory)
                        result.isChangedState(true)
                    }
                }
            }
        }

        /* 삭제 버튼 */
        binding.categoryInfoDeleteBtn.setOnClickListener {
            val alertDeleteDialog = TodoayAlertDialogFragment().apply {
                this.message = "카테고리를 삭제하면\n관련 TODO 모두 삭제됩니다.\n정말 삭제하시겠어요?"
                this.onClickListener = object : OnClickListener {
                    override fun onClick(item: Any) {
                        if (item as Boolean) {
                            service.deleteCategory(
                                category.id,
                                onResponse = {
                                    result.isChangedState(true)
                                    dismiss()
                                },
                                onErrorResponse = {
                                    mainAct.showShortToast("카테고리 삭제가 실패하였습니다.\n다시 시도해주세요")
                                },
                                onFailure = {}
                            )
                        }
                    }
                }
                this.show(this@CategoryInfoFragment.parentFragmentManager, this.tag)
            }
        }

        /* 종료 버튼 */
        binding.categoryInfoFinishBtn.setOnClickListener {
            TodoayAlertDialogFragment().apply {
                this.message = "카테고리를 종료하면\n이 카테고리의 TODO를 추가하지 못합니다.\n정말 종료하시겠어요?"
                this.onClickListener = object : OnClickListener {
                    override fun onClick(item: Any) {
                        if(item as Boolean) {
                            service.terminateCategory(
                                category.id,
                                onResponse = {

                                },
                                onErrorResponse = {

                                },
                                onFailure = {}
                            )
                        }
                    }
                }
                this.show(this@CategoryInfoFragment.parentFragmentManager, this.tag)
            }
        }

        return binding.root
    }

    private fun notifyCategoryDataChanged(newCategory : Category) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_category_color) as GradientDrawable
        drawable.setColor(Color.parseColor(newCategory.color))
        binding.categoryInfoColor.setImageDrawable(drawable)
        binding.categoryInfoName.text = newCategory.name
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