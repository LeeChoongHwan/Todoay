package com.todoay.view.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.adapter.CalendarInnerRVA
import com.todoay.adapter.CalendarRVA
import com.todoay.data.CalendarData
import com.todoay.data.CalendarInnerData
import com.todoay.databinding.FragmentCalanderMainBinding

class CalanderMainFragment : Fragment() {
    lateinit var calendarInnerRVA: CalendarInnerRVA
    lateinit var calendarRVA: CalendarRVA

    val mDatas =  mutableListOf<CalendarData>()
    val mDatas2 = mutableListOf<CalendarInnerData>()
    private var mBinding : FragmentCalanderMainBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCalanderMainBinding.inflate(inflater,container,false)


        mBinding = binding

        //알림 벨
        mBinding?.calanderMainBellIv?.setOnClickListener {

        }

        //달력 전환 버튼
        mBinding?.calanderMainCalanderSelect?.setOnClickListener {

        }
        //Todo 전환 버튼
        mBinding?.calanderMainTodoSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_calanderMainFragment_to_dueDateTodoMainFragment)
        }

        mBinding?.todoMainSetting?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_calanderMainFragment_to_categorySettingFragment)
        }

        initRecycler()



        return mBinding?.root
    }

    private fun initRecycler() {
        calendarInnerRVA = CalendarInnerRVA()
        calendarRVA = CalendarRVA()

        mBinding?.calanderMainRecyclerPlan?.adapter = calendarRVA

        mDatas.apply {
            add(CalendarData("운동",CalendarInnerData("스쿼트 50회","#오운완")))
        }

        mDatas2.apply {
            add(CalendarInnerData("스쿼트 50회","#오운완"))
        }

        calendarInnerRVA.dataList = mDatas2
        calendarRVA.dataList = mDatas

        calendarInnerRVA.notifyDataSetChanged()
        calendarRVA.notifyDataSetChanged()
    }


}

