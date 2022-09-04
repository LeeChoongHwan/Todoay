package com.todoay.view.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.adapter.TodoFinishRVA
import com.todoay.data.TodoFinishData
import com.todoay.databinding.FragmentTodoMainFinishBinding

class TodoMainFinishFragment: Fragment() {
    lateinit var todoFinishRVA: TodoFinishRVA
    private var mBinding : FragmentTodoMainFinishBinding ?= null
    
    val mDatas =  mutableListOf<TodoFinishData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTodoMainFinishBinding.inflate(inflater,container,false)
        mBinding = binding

        mBinding?.todoMainAftertodoIv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFinishFragment_to_dueDateTodoMainFragment)
        }

        mBinding?.todoMainCalanderSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFinishFragment_to_calanderMainFragment)
        }
        mBinding?.todoMainSetting?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFinishFragment_to_categorySettingFragment)
        }
        
        initRecycler()

        mBinding?.todoMainRecyclerview?.addItemDecoration(DueDateTodoMainFragment.VerticalItemDecorator(20))
        mBinding?.todoMainRecyclerview?.addItemDecoration(DueDateTodoMainFragment.HorizontalItemDecorator(20))

        return mBinding?.root
    }

    private fun initRecycler() {
        todoFinishRVA = TodoFinishRVA()
        mBinding?.todoMainRecyclerview?.adapter = todoFinishRVA
        
        mDatas.apply { 
            add(TodoFinishData("인공지능 중간고사","05/10"))
            add(TodoFinishData("데이터 중간고사","05/08"))
        }
        todoFinishRVA.dataList=mDatas
        todoFinishRVA.notifyDataSetChanged()



    }
}