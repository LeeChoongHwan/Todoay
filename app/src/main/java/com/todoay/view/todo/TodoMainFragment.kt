package com.todoay.view.todo

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomfragmenttest.AddDTodoFragment
import com.todoay.R
import com.todoay.adapter.TodoRVA
import com.todoay.data.TodoData
import com.todoay.databinding.FragmentTodoMainBinding


class TodoMainFragment : Fragment() {

    lateinit var todoRva : TodoRVA
    private var mBinding : FragmentTodoMainBinding ?= null

    val mDatas = mutableListOf<TodoData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTodoMainBinding.inflate(inflater,container,false)
        mBinding = binding

        mBinding?.todoMainAddbtn?.setOnClickListener {

        }
        mBinding?.todoMainCalanderSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFragment_to_calanderMainFragment)
        }
        mBinding?.todoMainAlign2?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFragment_to_todoMainAlignDuedateFragment2)
        }
        mBinding?.todoMainAftertodoIv?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFragment_to_todoMainFinishFragment)
        }
        mBinding?.todoMainSetting?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_todoMainFragment_to_categorySettingFragment)
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
        todoRva = TodoRVA()
        mBinding?.todoMainRecyclerview?.adapter = todoRva

        mDatas.apply {
            add(TodoData("D-1","긴급","비즈니스 중간고사","06/11"))
            add(TodoData("D-11","중간","인공지능 중간고사","06/21"))
        }
        todoRva.dataList = mDatas
        todoRva.notifyDataSetChanged()


    }


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