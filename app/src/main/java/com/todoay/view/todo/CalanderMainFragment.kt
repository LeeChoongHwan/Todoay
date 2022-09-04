package com.todoay.view.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.R
import com.todoay.adapter.CalendarRVA
import com.todoay.data.CalendarData
import com.todoay.databinding.FragmentCalanderMainBinding

class CalanderMainFragment : Fragment() {

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



        return mBinding?.root
    }



}

