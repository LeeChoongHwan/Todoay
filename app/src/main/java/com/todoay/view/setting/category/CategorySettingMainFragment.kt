package com.todoay.view.setting.category

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.view.adapter.category.CategoryAdapter
import com.todoay.view.adapter.category.CategoryFinishAdapter
import com.todoay.view.setting.category.interfaces.CategoryOnClickListener
import com.todoay.api.domain.category.CategoryAPI
import com.todoay.api.domain.category.dto.CategoryOrderIndexDto
import com.todoay.api.domain.category.dto.request.ModifyCategoryOrderIndexRequest
import com.todoay.data.category.Category
import com.todoay.databinding.FragmentCategorySettingMainBinding
import com.todoay.view.global.interfaces.CreateValueResult
import com.todoay.view.setting.category.interfaces.CategoryInfoResult
import com.todoay.view.setting.category.util.ItemTouchHelperCallback
import java.util.stream.Collectors

class CategorySettingMainFragment : Fragment() {

    enum class CategoryRvType { MAIN, FINISH }
    lateinit var currentRvType: CategoryRvType

    lateinit var binding : FragmentCategorySettingMainBinding
    lateinit var categoryAdapter : CategoryAdapter
    lateinit var categoryFinishAdapter: CategoryFinishAdapter

    /** Category API 서비스 */
    private val service by lazy { CategoryAPI.getInstance() }

    /** RvList 데이터 배열 변수 */
    private var categoryList : List<Category> = listOf()

    /** 종료 카테고리 리스트를 본 적이 있는지의 여부 */
    private var haveSeenFinishList : Boolean = false

    private var isChangedCategoryOrderIndex : Boolean = false

    /* 애니메이션 선언 */
    lateinit var slideFromRight : Animation
    lateinit var slideFromLeft : Animation
    lateinit var slideToRight : Animation
    lateinit var slideToLeft : Animation
    lateinit var flipFront : Animator
    lateinit var flipBack : Animator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategorySettingMainBinding.inflate(inflater, container, false)

        val mainLayout = binding.categorySettingMainLayout
        val finishLayout = binding.categorySettingMainFinishLayout

        initAnimation(requireContext())
        initListAdapter(requireContext())
        currentRvType = CategoryRvType.MAIN

        /* 뒤로가기 버튼 */
        binding.categorySettingMainBackBtn.setOnClickListener {
            if(currentRvType == CategoryRvType.MAIN) {
                if(isChangedCategoryOrderIndex) {
                    modifyCategoryOrderIndex()
                }
                Navigation.findNavController(requireView()).popBackStack()
            }
            else if(currentRvType == CategoryRvType.FINISH) {
                mainLayout.startAnimation(slideFromLeft)
                finishLayout.startAnimation(slideToRight)
                mainLayout.visibility = View.VISIBLE
                finishLayout.visibility = View.INVISIBLE
                currentRvType = CategoryRvType.MAIN

                binding.categorySettingMainPlusBtn.visibility = View.VISIBLE
                binding.categorySettingMainFinishListBtn.visibility = View.VISIBLE
            }
        }

        /* 카테고리 추가 버튼 */
        binding.categorySettingMainPlusBtn.setOnClickListener {
            val addCategoryFragment = AddCategoryFragment(categoryList.size)
            addCategoryFragment.show(parentFragmentManager, addCategoryFragment.tag)
            addCategoryFragment.createResult = object : CreateValueResult {
                override fun isCreate(isResult: Boolean) {
                    if(isResult) {
                        getCategoryList()
                    }
                }
            }
        }

        /* 종료 카테고리 버튼 */
        binding.categorySettingMainFinishListBtn.setOnClickListener {
            if(!haveSeenFinishList) initFinishListAdapter(requireContext())
            getFinishCategoryList()

            finishLayout.startAnimation(slideFromRight)
            mainLayout.startAnimation(slideToLeft)
            finishLayout.visibility = View.VISIBLE
            mainLayout.visibility = View.INVISIBLE
            currentRvType = CategoryRvType.FINISH

            binding.categorySettingMainPlusBtn.visibility = View.INVISIBLE
            binding.categorySettingMainFinishListBtn.visibility = View.INVISIBLE
        }

        return binding.root
    }

    private fun modifyCategoryOrderIndex() {
        val changeOrderIndexList: List<CategoryOrderIndexDto> = categoryAdapter.dataList.stream()
            .map { c -> CategoryOrderIndexDto(c.id, c.orderIndex) }
            .collect(Collectors.toList())
        service.modifyCategoryOrderIndex(
            ModifyCategoryOrderIndexRequest(changeOrderIndexList),
            onResponse = {},
            onErrorResponse = {
                mainAct!!.showShortToast("카테고리 순서 변경이 실패하였습니다...\n다시 시도해주세요")
            },
            onFailure = {}
        )
    }

    private fun initListAdapter(context: Context) {
        binding.categorySettingMainList.layoutManager = LinearLayoutManager(context)
        categoryAdapter = CategoryAdapter(context)
        categoryAdapter.onClickListener = object : CategoryOnClickListener {
            override fun onClick(category: Category) {
                val categoryInfo = CategoryInfoFragment(category)
                categoryInfo.show(parentFragmentManager, categoryInfo.tag)
                categoryInfo.result = object : CategoryInfoResult {
                    override fun isChangedState(isChanged: Boolean) {
                        if(isChanged) {
                            getCategoryList()
                        }
                    }
                }
            }
            override fun isChangedOrderIndex(isChanged: Boolean) {
                isChangedCategoryOrderIndex = isChanged
            }
        }
        // ItemTouchHelper 지정 -> 정렬 순서 변경을 위해 Item(Category) Drag & Drop
        ItemTouchHelper(ItemTouchHelperCallback(categoryAdapter)).attachToRecyclerView(binding.categorySettingMainList)

        getCategoryList()
        binding.categorySettingMainList.adapter = categoryAdapter
    }

    private fun initFinishListAdapter(context: Context) {
        binding.categorySettingMainFinishList.layoutManager = LinearLayoutManager(context)
        categoryFinishAdapter = CategoryFinishAdapter(context)
        categoryFinishAdapter.onClickListener = object : CategoryOnClickListener {
            override fun onClick(category: Category) {
                val categoryFinishInfo = CategoryFinishInfoFragment(category)
                categoryFinishInfo.show(parentFragmentManager, categoryFinishInfo.tag)
                categoryFinishInfo.result = object : CategoryInfoResult {
                    override fun isChangedState(isChanged: Boolean) {
                        if(isChanged) {
                            getFinishCategoryList()
                        }
                    }

                }
            }
            override fun isChangedOrderIndex(isChanged: Boolean) {
                // do nothing
            }
        }
        binding.categorySettingMainFinishList.adapter = categoryFinishAdapter
        haveSeenFinishList = true
    }

    private fun getCategoryList() {
        service.readCategory(
            onResponse = {
                categoryList = it.categoryList.stream()
                    .filter { c -> !c.isEnded }
                    .map { c -> Category(c.id, c.name, c.color, c.orderIndex, c.isEnded) }
                    .sorted(Comparator.comparing(Category::orderIndex))
                    .collect(Collectors.toList())
                categoryAdapter.dataList = categoryList as ArrayList<Category>
                categoryAdapter.notifyDataSetChanged()
            },
            onErrorResponse = {
                mainAct!!.showShortToast("카테고리 조회에 실패하였습니다.\n다시 시도해주세요")
            },
            onFailure = {}
        )
    }

    private fun getFinishCategoryList() {
        service.readCategory(
            onResponse = {
                val categoryFinishList = it.categoryList.stream()
                    .filter { c -> c.isEnded }
                    .map { c -> Category(c.id, c.name, c.color, c.orderIndex, c.isEnded) }
                    .collect(Collectors.toList())
                categoryFinishAdapter.dataList = categoryFinishList
                categoryFinishAdapter.notifyDataSetChanged()
            },
            onErrorResponse = {
                mainAct!!.showShortToast("카테고리 조회에 실패하였습니다.\n다시 시도해주세요")
            },
            onFailure = {}
        )
    }

    private fun initAnimation(context : Context) {
        slideFromRight = AnimationUtils.loadAnimation(context, R.anim.slide_from_right)
        slideFromLeft = AnimationUtils.loadAnimation(context, R.anim.slide_from_left)
        slideToRight = AnimationUtils.loadAnimation(context, R.anim.slide_to_right)
        slideToLeft = AnimationUtils.loadAnimation(context, R.anim.slide_to_left)
        flipFront = AnimatorInflater.loadAnimator(context, R.anim.flip_front)
        flipBack = AnimatorInflater.loadAnimator(context, R.anim.flip_back)
    }

}