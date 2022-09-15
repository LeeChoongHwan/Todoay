package com.todoay.view.todo.dueDate

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.adapter.todo.dueDate.DueDateTodoFinishRvAdapter
import com.todoay.adapter.todo.dueDate.DueDateTodoRvAdapter
import com.todoay.api.domain.todo.dueDate.DueDateTodoAPI
import com.todoay.api.domain.todo.dueDate.dto.response.ReadAllDueDateTodoResponse
import com.todoay.data.todo.dueDate.DueDate
import com.todoay.databinding.FragmentDueDateTodoMainBinding
import com.todoay.view.global.interfaces.CreateValueResult
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult
import com.todoay.view.todo.common.interfaces.TodoOnClickIdListener
import java.util.stream.Collectors

class DueDateTodoMainFragment : Fragment() {

    /** DueDateTodo Recycler View Type */
    enum class DueDateTodoRvType { /** Main Recycler View */MAIN, /** Finish Recycler View */FINISH }

    lateinit var mainAdapter : DueDateTodoRvAdapter
    lateinit var finishAdapter: DueDateTodoFinishRvAdapter

    private var mBinding : FragmentDueDateTodoMainBinding ?= null

    private val service by lazy { DueDateTodoAPI.getInstance() }

    /**
     * 리스트 정렬 타입 String 변수
     * Type: importance, duedate
     */
    private lateinit var orderType : String

    /* 애니메이션 선언 */
    lateinit var slideFromRight : Animation
    lateinit var slideFromLeft : Animation
    lateinit var slideToRight : Animation
    lateinit var slideToLeft : Animation
    lateinit var flipFront : Animator
    lateinit var flipBack : Animator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDueDateTodoMainBinding.inflate(inflater,container,false)
        mBinding = binding

        initAnimation(requireContext())

        /* DailyTodo 전환 버튼 */
        mBinding?.dueDateTodoMainCalanderSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_dueDateTodoMainFragment_to_dailyTodoMainFragment)
        }
        /* 우선순위순 버튼 */
        orderType = "importance" // initialize
        binding.dueDateTodoMainOrderTypeGroup.setOnCheckedChangeListener { _, id ->
            when(id) {
                R.id.due_date_todo_main_order_importance -> {
                    orderType = "importance"
                    getDueDateList(orderType)
                }
                R.id.due_date_todo_main_order_due_date -> {
                    orderType = "duedate"
                    getDueDateList(orderType)
                }
            }
        }
        /* 완료Todo 버튼 */
        val mainRvLayout = binding.dueDateTodoMainRvLayout
        val finishRvLayout = binding.dueDateTodoMainFinishRvLayout
        mBinding?.dueDateTodoMainFinishedIv?.setOnClickListener {
            // Main -> Finish
            if(finishRvLayout.visibility == View.INVISIBLE) {
                finishRvLayout.startAnimation(slideFromRight)
                mainRvLayout.startAnimation(slideToLeft)
                finishRvLayout.visibility = View.VISIBLE
                mainRvLayout.visibility = View.INVISIBLE
                changeRvTypeForButtonVisibility(DueDateTodoRvType.FINISH)
            }
            // Finish -> Main
            else {
                mainRvLayout.startAnimation(slideFromLeft)
                finishRvLayout.startAnimation(slideToRight)
                mainRvLayout.visibility = View.VISIBLE
                finishRvLayout.visibility = View.INVISIBLE
                changeRvTypeForButtonVisibility(DueDateTodoRvType.MAIN)
            }
        }
        /* 환경설정 버튼 */
        mBinding?.dueDateTodoMainSetting?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_dueDateTodoMainFragment_to_categorySettingMainFragment)
        }
        /* DueDateTodo 추가 버튼 */
        mBinding?.dueDateTodoMainAddBtn?.setOnClickListener {
            val addDueDateTodoFragment = AddDueDateTodoFragment()
            addDueDateTodoFragment.show(parentFragmentManager, addDueDateTodoFragment.tag)
            addDueDateTodoFragment.createResult = object : CreateValueResult {
                override fun isCreate(isResult: Boolean) {
                    if(isResult) {
                        getDueDateList(orderType)
                    }
                }
            }
        }

        initRecycler(mBinding)

        return mBinding?.root
    }

    private fun changeRvTypeForButtonVisibility(type : DueDateTodoRvType) {
        val orderTypeGroup = mBinding?.dueDateTodoMainOrderTypeGroup
        val addButton = mBinding?.dueDateTodoMainAddBtn
        // Main Rv -> Finish Rv
        if(type == DueDateTodoRvType.MAIN) {
            orderTypeGroup?.startAnimation(slideFromLeft)
            addButton?.startAnimation(slideFromLeft)
            orderTypeGroup?.visibility = View.VISIBLE
            addButton?.visibility = View.VISIBLE
        }
        // Finish Rv -> Main Rv
        else if(type == DueDateTodoRvType.FINISH) {
            addButton?.startAnimation(slideToLeft)
            orderTypeGroup?.startAnimation(slideToLeft)
            addButton?.visibility = View.INVISIBLE
            orderTypeGroup?.visibility = View.INVISIBLE
        }
    }

    private fun initAnimation(context : Context) {
        slideFromRight = AnimationUtils.loadAnimation(context, R.anim.slide_from_right)
        slideFromLeft = AnimationUtils.loadAnimation(context, R.anim.slide_from_left)
        slideToRight = AnimationUtils.loadAnimation(context, R.anim.slide_to_right)
        slideToLeft = AnimationUtils.loadAnimation(context, R.anim.slide_to_left)
        flipFront = AnimatorInflater.loadAnimator(context, R.anim.flip_front)
        flipBack = AnimatorInflater.loadAnimator(context, R.anim.flip_back)
    }

    private fun initRecycler(binding: FragmentDueDateTodoMainBinding?) {
        initMainRecycler(binding?.dueDateTodoMainRecyclerview)
        initFinishRecycler(binding?.dueDateTodoMainFinishRecyclerview)
    }

    private fun initMainRecycler(view: RecyclerView?) {
        mainAdapter = DueDateTodoRvAdapter()
        mainAdapter.onClickListener = object : TodoOnClickIdListener {
            override fun onClick(id: Long) {
                val infoDialog = DueDateTodoInfoFragment(id)
                infoDialog.show(parentFragmentManager, infoDialog.tag)
                infoDialog.result = object : TodoInfoChangedStateResult {
                    override fun isChangedState(isChanged: Boolean) {
                        if(isChanged) {
                            getDueDateList(orderType)
                        }
                    }
                }
            }
        }
        getDueDateList(orderType)
        view?.adapter = mainAdapter
        view?.addItemDecoration(VerticalItemDecorator(20))
        view?.addItemDecoration(HorizontalItemDecorator(20))
    }

    private fun initFinishRecycler(view: RecyclerView?) {
        finishAdapter = DueDateTodoFinishRvAdapter()
        finishAdapter.onClickListener = object : DueDateTodoFinishRvAdapter.DueDateTodoFinishOnClickListener {
            override fun onClick(id: Long) {
                val infoDialog = DueDateTodoInfoFragment(id)
                infoDialog.show(parentFragmentManager, infoDialog.tag)
                infoDialog.result = object : TodoInfoChangedStateResult {
                    override fun isChangedState(isChanged: Boolean) {
                        if(isChanged) {
                            getFinishDueDateList()
                        }
                    }
                }
            }
        }
        getFinishDueDateList()
        view?.adapter = finishAdapter
        view?.addItemDecoration(VerticalItemDecorator(20))
        view?.addItemDecoration(HorizontalItemDecorator(20))
    }

    private fun getDueDateList(orderType : String) {
        service.readDueDateTodoList(
            orderType,
            onResponse = { responseList ->
                mainAdapter.dataList = convertResponseListToDueDateList(responseList)
                mainAdapter.notifyDataSetChanged()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

    private fun getFinishDueDateList() {
        service.readFinishDueDateTodoList(
            onResponse = { responseList ->
                finishAdapter.dataList = convertResponseListToDueDateList(responseList)
                finishAdapter.notifyDataSetChanged()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

    /**
     * List<[ReadAllDueDateTodoResponse]>를 List<[DueDate]>로 변환하는 메소드.
     * Response 리스트의 요소들을 Data 객체(DueDate)로 변환한 리스트를 리턴한다.
     *
     * @param responseList ReadAllDueDateTodoResponse 객체가 저장되어 있는 Response List
     * @return DueDate 객체가 저장되어 있는 List
     */
    private fun convertResponseListToDueDateList(responseList : List<ReadAllDueDateTodoResponse>) : List<DueDate> {
        return responseList.stream()
            .map { response -> DueDate(
                id = response.id,
                todo = response.todo,
                dueDate = response.dueDate,
                priority = response.priority,
                hashtagList = response.hashtagList
            ) }
            .collect(Collectors.toList())
    }

    class VerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {

        @Override
        override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
            outRect.bottom = divHeight
        }
    }

    class HorizontalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {

        @Override
        override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = divHeight
            outRect.right = divHeight
        }
    }

}
