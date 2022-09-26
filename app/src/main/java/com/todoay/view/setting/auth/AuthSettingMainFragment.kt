package com.todoay.view.setting.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.TodoayApplication
import com.todoay.databinding.FragmentAuthSettingMainBinding
import com.todoay.databinding.ListItemSettingBinding
import com.todoay.view.global.interfaces.OnClickListener

class AuthSettingMainFragment : Fragment() {

    lateinit var binding : FragmentAuthSettingMainBinding

    lateinit var authSettingTopAdapter : AuthSettingAdapter
    lateinit var authSettingBotAdapter : AuthSettingAdapter

    private val authSettingProfile = "프로필"
    private val authSettingModifyPassword = "비밀번호 재설정"
    private val authSettingWithdrawal = "계정 삭제"

    private val topList : List<String> = listOf(authSettingProfile, authSettingModifyPassword)
    private val botList : List<String> = listOf(authSettingWithdrawal)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthSettingMainBinding.inflate(inflater, container, false)

        /* back button */
        binding.authSettingMainBackBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_authSettingMainFragment_to_settingMainFragment)
        }

        /* user email text */
        binding.authSettingMainUserEmailTv.text = TodoayApplication.pref.getEmail()

        binding.authSettingMainTopList.layoutManager = LinearLayoutManager(requireContext())
        authSettingTopAdapter = AuthSettingAdapter(requireContext()).apply {
            this.dataList = topList
            this.onClickListener = object : OnClickListener {
                override fun onClick(item: Any) {
                    doSelectedSetting(item)
                }

            }
        }
        binding.authSettingMainTopList.adapter = authSettingTopAdapter

        binding.authSettingMainBottomList.layoutManager = LinearLayoutManager(requireContext())
        authSettingBotAdapter = AuthSettingAdapter(requireContext()).apply {
            this.dataList = botList
            this.onClickListener = object : OnClickListener {
                override fun onClick(item: Any) {
                    doSelectedSetting(item)
                }
            }
        }
        binding.authSettingMainBottomList.adapter = authSettingBotAdapter

        return binding.root
    }

    private fun doSelectedSetting(setting : Any) {
        when (setting as String) {
            authSettingProfile -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_authSettingMainFragment_to_profileFragment)
            }
            authSettingModifyPassword -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_authSettingMainFragment_to_changePasswordFragment)
            }
            authSettingWithdrawal -> {
                mainAct.showShortToast("서비스 준비 중입니다")
            }
            else -> {}
        }
    }

    class AuthSettingAdapter(private val context : Context) : RecyclerView.Adapter<AuthSettingAdapter.ViewHolder>() {

        var dataList : List<String> = listOf()

        lateinit var onClickListener : OnClickListener

        class ViewHolder(val binding : ListItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
            var contentName = binding.settingContentName
            var arrow = binding.settingContentArrow
            var contentField = binding.settingContentBg
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ListItemSettingBinding.inflate(LayoutInflater.from(context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.contentName.text = dataList[position]

            if(dataList[position] == "계정 삭제") {
                holder.contentName.setTextColor(context.resources.getColor(R.color.red))
                holder.arrow.text = ""
            }

            holder.contentField.setOnClickListener {
                onClickListener.onClick(dataList[position])
            }
        }

        override fun getItemCount(): Int = dataList.size
    }
}