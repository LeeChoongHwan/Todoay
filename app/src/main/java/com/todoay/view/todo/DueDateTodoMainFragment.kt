package com.todoay.view.todo

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.adapter.TodoRVA
import com.todoay.api.domain.todo.dueDate.DueDateTodoAPI
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.data.todo.DueDate
import com.todoay.databinding.FragmentDueDateTodoMainBinding
import com.todoay.global.util.Utils.Companion.printLog
import java.util.stream.Collectors


class DueDateTodoMainFragment : Fragment() {

    lateinit var todoRva : TodoRVA

    private var mBinding : FragmentDueDateTodoMainBinding ?= null
    private var service : DueDateTodoAPI = DueDateTodoAPI()

    private lateinit var orderType : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDueDateTodoMainBinding.inflate(inflater,container,false)
        mBinding = binding

        /* DailyTodo 전환 버튼 */
        mBinding?.dueDateTodoMainCalanderSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_dueDateTodoMainFragment_to_calanderMainFragment)
        }
        /* 우선순위순 버튼 */
        orderType = "importance" // initialize
        binding.dueDateTodoMainOrderTypeGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id) {
                R.id.due_date_todo_main_order_importance -> {
                    orderType = "importance"
                    readList(orderType)
                }
                R.id.due_date_todo_main_order_due_date -> {
                    orderType = "duedate"
                    readList(orderType)
                }
            }
        }


        /* 완료Todo 버튼 */
        mBinding?.dueDateTodoMainFinishedIv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_dueDateTodoMainFragment_to_todoMainFinishFragment)
        }
        /* 환경설정 버튼 */
        mBinding?.dueDateTodoMainSetting?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_dueDateTodoMainFragment_to_categorySettingFragment)
        }
        /* DueDateTodo 추가 버튼 */
        mBinding?.dueDateTodoMainAddBtn?.setOnClickListener {
            val addDTodoFragment = AddDTodoFragment()
            addDTodoFragment.show(parentFragmentManager, addDTodoFragment.tag)
            addDTodoFragment.result = object : AddDTodoFragment.CreateDueDateTodoResult {
                override fun isCreate(isResult: Boolean) {
                    if(isResult) {
                        readList(orderType)
                    }
                }

            }
        }

        initRecycler()

        mBinding?.dueDateTodoMainRecyclerview?.addItemDecoration(VerticalItemDecorator(20))
        mBinding?.dueDateTodoMainRecyclerview?.addItemDecoration(HorizontalItemDecorator(20))


        return mBinding?.root
    }

    private fun initRecycler() {
        todoRva = TodoRVA()
        readList(orderType)
        mBinding?.dueDateTodoMainRecyclerview?.adapter = todoRva
    }

    private fun readList(orderType : String) {
        printLog("$orderType 호출")
        service.readAllDueDateTodo(
            orderType,
            onResponse = {
                val resultList : List<DueDate> = it.stream()
                    .map { response -> DueDate(
                        id = response.id,
                        todo = response.todo,
                        dueDate = response.dueDate,
                        priority = response.priority,
                        hashtagList = response.hashtagList
                    ) }
                    .collect(Collectors.toList())
                todoRva.dataList = resultList
                todoRva.notifyDataSetChanged()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
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
