package com.todoay.view.setting.category

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.api.domain.category.CategoryAPI
import com.todoay.api.domain.category.dto.request.CreateCategoryRequest
import com.todoay.api.domain.category.dto.request.ModifyCategoryRequest
import com.todoay.data.category.Category
import com.todoay.databinding.FragmentAddCategoryBinding
import com.todoay.global.util.PrintUtil.printLog
import com.todoay.view.global.interfaces.CreateValueResult
import com.todoay.view.setting.category.interfaces.ModifiedCategoryResult

class AddCategoryFragment(private val orderIndex : Int) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentAddCategoryBinding

    var categoryName: String = ""
    var categoryColor : String = ""
    var isCategoryInput : Boolean = false

    /* 수정 모드 */
    lateinit var modifiedCategory : Category
    var isModificationMode : Boolean = false
    var isModify : Boolean = true

    lateinit var createResult : CreateValueResult
    lateinit var modifiedResult : ModifiedCategoryResult

    private val service by lazy { CategoryAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        isCancelable = false

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_category_color) as GradientDrawable
        if(isModificationMode) {
            binding.addCategoryEt.setText(modifiedCategory.name)
            drawable.setColor(Color.parseColor(modifiedCategory.color))
            binding.addCategoryColorBtn.setImageDrawable(drawable)
            binding.addCategoryConfirmBtn.text = "수정하기"
            isCategoryInput = true
            isModify = false
        }
        else {
            drawable.setColor(requireContext().resources.getColor(R.color.main_color))
            binding.addCategoryColorBtn.setImageDrawable(drawable)
        }

        /* 닫기(취소) 버튼 */
        binding.addCategoryCancelBtn.setOnClickListener {
            dismiss()
        }

        /* 카테고리 이름 et 필드 */
        binding.addCategoryEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.addCategoryEt.text.toString().isNotBlank()) {
                    isCategoryInput = true
                    isModify = true
                    checkConfirmButtonEnable()
                } else {
                    isCategoryInput = false
                    checkConfirmButtonEnable()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /* 디폴트 컬러 */
        if(!isModificationMode) {
            categoryColor = resources.getString(R.color.main_color)
        }

        /* 색상 변경 버튼 */
        binding.addCategoryColorBtn.setOnClickListener {
            val categoryColorSettingDialog = CategoryColorSettingDialog()
            categoryColorSettingDialog.show(parentFragmentManager, categoryColorSettingDialog.tag)
            categoryColorSettingDialog.result = object : CategoryColorSettingDialog.CategoryColorResult {
                override fun getColor(colorCode: String) {
                    categoryColor = colorCode
                    drawable.setColor(Color.parseColor(categoryColor))
                    binding.addCategoryColorBtn.setImageDrawable(drawable)
                    isModify = true
                    checkConfirmButtonEnable()
                }
            }
        }

        /* 추가하기 버튼 */
        binding.addCategoryConfirmBtn.setOnClickListener {
            categoryName = binding.addCategoryEt.text.toString()
            if(!isModificationMode) {
                createCategory()
            } else {
                modifyCategory()
            }
        }

        return binding.root
    }

    private fun checkConfirmButtonEnable() {
        if(isCategoryInput && isModify) {
            binding.addCategoryConfirmBtn.isEnabled = true
            binding.addCategoryConfirmBtn.setBackgroundResource(R.drawable.bg_primary_btn)
        } else {
            binding.addCategoryConfirmBtn.isEnabled = false
            binding.addCategoryConfirmBtn.setBackgroundResource(R.drawable.bg_primary_fail_btn)
        }
    }

    private fun createCategory() {
        val request = CreateCategoryRequest(
            name = categoryName,
            color = categoryColor,
            orderIndex = orderIndex
        )
        service.createCategory(
            request,
            onResponse = {
                createResult.isCreate(true)
                dismiss()
            },
            onErrorResponse = {
                mainAct!!.showShortToast("카테고리 추가가 실패하였습니다.\n다시 시도해주세요")
            },
            onFailure = {}
        )
    }

    private fun modifyCategory() {
        modifiedCategory.name = categoryName
        modifiedCategory.color = categoryColor
        val request = ModifyCategoryRequest(
            name = categoryName,
            color = categoryColor
        )
        service.modifyCategory(
            modifiedCategory.id,
            request,
            onResponse = {
                modifiedResult.isModified(true, modifiedCategory)
                dismiss()
            },
            onErrorResponse = {
                mainAct!!.showShortToast("카테고리 수정이 실패하였습니다.\n다시 시도해주세요")
            },
            onFailure = {}
        )
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