package com.todoay.view.todo.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.todoay.R
import com.todoay.view.adapter.todo.daily.DailyTodoCategoryRvAdapter
import com.todoay.api.domain.category.CategoryAPI
import com.todoay.api.domain.todo.common.TodoAPI
import com.todoay.api.domain.todo.daily.DailyTodoAPI
import com.todoay.data.category.Category
import com.todoay.data.todo.daily.Alarm
import com.todoay.data.todo.daily.Daily
import com.todoay.data.todo.daily.DailyInfo
import com.todoay.databinding.FragmentDailyTodoMainBinding
import com.todoay.view.global.interfaces.CreateValueResult
import com.todoay.view.todo.common.interfaces.TodoInfoChangedStateResult
import com.todoay.view.todo.daily.interfaces.DailyOnClickListenerForGetCategory
import com.todoay.view.todo.daily.interfaces.DailyOnClickListenerForGetDaily
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.stream.Collectors

class DailyTodoMainFragment : Fragment() {

    private var mBinding : FragmentDailyTodoMainBinding ?= null

    private lateinit var dailyTodoCategoryAdapter: DailyTodoCategoryRvAdapter

    private lateinit var date : LocalDate

    private val commonService by lazy { TodoAPI.getInstance() }
    private val categoryService by lazy { CategoryAPI.getInstance() }
    private val dailyService by lazy { DailyTodoAPI.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDailyTodoMainBinding.inflate(inflater,container,false)
        mBinding = binding

        val calendarView = binding.dailyTodoMainDailyTodoview
        date = Instant.ofEpochMilli(calendarView.date).atZone(ZoneId.systemDefault()).toLocalDate()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = LocalDate.of(year, month+1, dayOfMonth)
            getDailyTodoData()
        }

        /* 프로필 버튼 */
        mBinding?.dailyTodoMainProfileBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_profileFragment)
        }

        // 설정 버튼
        mBinding?.dailyTodoMainSettingBtn?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_settingMainFragment)
        }

        // 투두 전환 버튼
        mBinding?.dailyTodoMainTodoSelect?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_dailyTodoMainFragment_to_dueDateTodoMainFragment)
        }

        initRecycler()

        return mBinding?.root
    }

    private fun initRecycler() {
        dailyTodoCategoryAdapter = DailyTodoCategoryRvAdapter(requireContext()).apply {
            this.onClickListenerForAddButton = object : DailyOnClickListenerForGetCategory {
                override fun onClick(category : Category) {
                    openAddDailyDialog(category)
                }
            }
            this.onClickListenerForDotButton = object : DailyOnClickListenerForGetDaily {
                override fun onClick(daily: Daily) {
                    getDailyInfo(daily.id, true)
                }
            }
            this.onClickListenerForCheckBox = object : DailyOnClickListenerForGetDaily {
                override fun onClick(daily: Daily) {
                    switchFinish(daily.id)
                }
            }
        }
        getCategoryData()
        mBinding?.dailyTodoMainRecyclerPlan?.adapter = dailyTodoCategoryAdapter
    }

    private fun switchFinish(id : Long) {
        commonService.switchTodoFinishState(
            id,
            onResponse = {
                getDailyInfo(id, false)
            },
            onErrorResponse = {},
            onFailure = {}
        )
    }

    private fun openAddDailyDialog(category: Category) {
        val addDailyTodoFragment = AddDailyTodoFragment(date, category)
        addDailyTodoFragment.show(parentFragmentManager, addDailyTodoFragment.tag)
        addDailyTodoFragment.createResult = object : CreateValueResult {
            override fun isCreate(isCreate: Boolean) {
                if (isCreate) {
                    getDailyTodoData()
                }
            }
        }
    }

    private fun openDailyInfoMenu(dailyInfo : DailyInfo) {
        DailyTodoInfoMenuFragment(dailyInfo).apply {
            this.result = object : TodoInfoChangedStateResult {
                override fun isChangedState(isModified: Boolean, isDeleted: Boolean) {
                    if(isModified || isDeleted) {
                        dismiss()
                        getDailyTodoData()
                    }
                }
            }
            this.show(this@DailyTodoMainFragment.parentFragmentManager, this.tag)
        }
    }

    private fun getCategoryData() {
        categoryService.readCategory(
            onResponse = {
                dailyTodoCategoryAdapter.dailyCategoryList = it.categoryList.stream()
                    .filter { c -> !c.isEnded }
                    .map { c -> Category(c.id, c.name, c.color, c.orderIndex, c.isEnded) }
                    .sorted(Comparator.comparing(Category::orderIndex))
                    .collect(Collectors.toList())
                getDailyTodoData()
            },
            onErrorResponse = {},
            onFailure = {}
        )
    }

    private fun getDailyTodoData() {
        dailyService.readDailyTodoList(
            date,
            onResponse = { responseDailyList ->
                dailyTodoCategoryAdapter.dailyTodoHashMap = responseDailyList.stream()
                    .map { d -> Daily(d.id, d.todo, d.hashtagList, d.isPublic, d.isFinished, d.categoryId) }
                    .collect(Collectors.groupingBy(Daily::categoryId)) as HashMap<Long, ArrayList<Daily>>
                dailyTodoCategoryAdapter.notifyDataSetChanged()
            },
            onErrorResponse = {},
            onFailure = {}
        )
    }

    private fun getDailyInfo(id : Long, isOpen : Boolean) {
        dailyService.readDailyTodo(
            id,
            onResponse = { dto ->
                val dailyInfo = DailyInfo(
                    id = dto.id,
                    todo = dto.todo,
                    alarm = if(dto.alarm!=null) Alarm(dto.time, dto.alarm) else null,
                    time = dto.time,
                    location = dto.location,
                    partner = dto.partner,
                    date = dto.date,
                    category = Category(
                        dto.categoryInfoDto.id,
                        dto.categoryInfoDto.name,
                        dto.categoryInfoDto.color
                    ),
                    hashtagList = dto.hashtagList,
                    isPublic = dto.isPublic,
                    isFinish = dto.isFinish
                )

                if(isOpen) {
                    openDailyInfoMenu(dailyInfo)
                }
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

}

