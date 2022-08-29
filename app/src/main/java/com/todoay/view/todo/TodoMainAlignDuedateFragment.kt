package com.todoay.view.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.adapter.TodoRVA
import com.todoay.data.TodoData
import com.todoay.databinding.FragmentTodoMainAlignDuedateBinding

class TodoMainAlignDuedateFragment : Fragment() {

    lateinit var todoRVA: TodoRVA
    private var mBinding : FragmentTodoMainAlignDuedateBinding ?= null

    val mDatas = mutableListOf<TodoData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTodoMainAlignDuedateBinding.inflate(inflater,container,false)
        mBinding = binding

        mBinding?.todoMainAlign1?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainAlignDuedateFragment_to_todoMainFragment)
        }
        mBinding?.todoMainCalanderSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainAlignDuedateFragment_to_calanderMainFragment)
        }
        mBinding?.todoMainAftertodoIv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainAlignDuedateFragment_to_todoMainFinishFragment)
        }
        mBinding?.todoMainSetting?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainAlignDuedateFragment_to_categorySettingFragment)
        }
        mBinding?.todoMainAddbtn?.setOnClickListener {
            val addDTodoFragment = AddDTodoFragment()
            addDTodoFragment.show(parentFragmentManager, addDTodoFragment.tag)
        }

        initRecycler()

        mBinding?.todoMainRecyclerview?.addItemDecoration(VerticalItemDecorator(20))
        mBinding?.todoMainRecyclerview?.addItemDecoration(HorizontalItemDecorator(20))

        return mBinding?.root
    }

    private fun initRecycler() {
        todoRVA = TodoRVA()
        mBinding?.todoMainRecyclerview?.adapter = todoRVA
        mDatas.apply {
            add(TodoData("D-1","긴급","비즈니스 중간고사","06/11"))
            add(TodoData("D-11","중간","인공지능 중간고사","06/21"))
        }

        todoRVA.dataList = mDatas
        todoRVA.notifyDataSetChanged()



    }
}

