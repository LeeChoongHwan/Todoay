package com.todoay.view.adapter.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoay.databinding.ListItemSettingBinding
import com.todoay.view.global.interfaces.OnClickListener

class SettingAdapter(val context : Context) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    var dataList : List<String> = listOf()

    lateinit var onClickListener : OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemSettingBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentName.text = dataList[position]
        
        if(dataList[position] == "버전") {
            holder.arrow.text = "1.0"
            holder.listField.isClickable = false
            holder.listField.isEnabled = false
        }
        if(dataList[position] == "로그아웃") {
            holder.arrow.text = ""
        }

        holder.listField.setOnClickListener {
            onClickListener.onClick(dataList[position])
        }
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(val binding : ListItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
        var contentName = binding.settingContentName
        var listField = binding.settingContentBg
        var arrow = binding.settingContentArrow
    }
}