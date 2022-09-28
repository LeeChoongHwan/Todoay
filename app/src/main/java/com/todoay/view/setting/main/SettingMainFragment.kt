package com.todoay.view.setting.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.view.adapter.setting.SettingAdapter
import com.todoay.databinding.FragmentSettingMainBinding
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.OnClickListener

class SettingMainFragment : Fragment() {

    lateinit var binding : FragmentSettingMainBinding

    lateinit var settingTopAdapter : SettingAdapter
    lateinit var settingBotAdapter : SettingAdapter

    private val settingAuth = "계정"
    private val settingCategory = "카테고리"
    private val settingVersion = "버전"
    private val settingLogout = "로그아웃"

    private val settingTopList = listOf(settingAuth, settingCategory)
    private val settingBotList = listOf(settingVersion, settingLogout)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingMainBinding.inflate(inflater, container, false)

        binding.settingMainBackBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_dailyTodoMainFragment)
        }

        binding.settingMainTopList.layoutManager = LinearLayoutManager(requireContext())
        settingTopAdapter = SettingAdapter(requireContext()).apply {
            this.dataList = settingTopList
        }
        settingTopAdapter.onClickListener = object : OnClickListener {
            override fun onClick(item: Any) {
                openSelectedSetting(item)
            }
        }
        binding.settingMainTopList.adapter = settingTopAdapter

        binding.settingMainBottomList.layoutManager = LinearLayoutManager(requireContext())
        settingBotAdapter = SettingAdapter((requireContext())).apply {
            this.dataList = settingBotList
        }
        settingBotAdapter.onClickListener = object : OnClickListener {
            override fun onClick(item: Any) {
                openSelectedSetting(item)
            }
        }
        binding.settingMainBottomList.adapter = settingBotAdapter

        return binding.root
    }

    fun openSelectedSetting(setting : Any) {
        when(setting as String) {
            settingAuth -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_settingMainFragment_to_authSettingMainFragment)
            }
            settingCategory -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_settingMainFragment_to_categorySettingMainFragment)
            }
            settingLogout -> {
                TodoayAlertDialogFragment().apply {
                    this.message = "로그아웃하시겠습니까?"
                    this.onClickListener = object : OnClickListener {
                        override fun onClick(item: Any) {
                            val positive = item as Boolean
                            if (positive) {
                                mainAct.logout(null)
                            }
                        }
                    }
                    this.show(this@SettingMainFragment.parentFragmentManager, this.tag)
                }
            }
            else -> {}
        }
    }
}