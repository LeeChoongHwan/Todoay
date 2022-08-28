package com.example.bottomfragmenttest

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.databinding.FragmentHashtagSearchDialogBinding
import com.todoay.databinding.ListItemHashtagBinding
import java.util.*
import kotlin.collections.ArrayList

class HashtagSearchDialog : DialogFragment() {

    lateinit var binding : FragmentHashtagSearchDialogBinding
    lateinit var result : HashtagSearchDialogResult
    var currentHashtag: String? = null
    lateinit var resultHashtagList : ArrayList<String>
    /* Recycler View List */
    var hashtagList: ArrayList<HashtagItem> = ArrayList()

    /* 해시태그 검색 시작 인덱스 (해시태그 클릭 시 edittext의 텍스트 변경에 사용) */
    var hashtagStartIndex = 0

    interface HashtagSearchDialogResult {
        fun getResultList(hashtagResult: List<String>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHashtagSearchDialogBinding.inflate(inflater, container, false)
        isCancelable = false

        binding.hashtagSearchDialogList.layoutManager = HashtagListLinearLayout(requireContext())
        val adapter = HashtagAdapter(hashtagList)
        adapter.onClickListener = object : HashtagAdapter.HashtagOnClickListener {
            override fun onClick(hashtag: String) {
                StringBuilder(binding.hashtagSearchDialogHashtagEt.text.toString()).run {
                    this.delete(hashtagStartIndex, binding.hashtagSearchDialogHashtagEt.text.length)
                    this.insert(hashtagStartIndex, hashtag)
                    binding.hashtagSearchDialogHashtagEt.setText(this)
                    binding.hashtagSearchDialogHashtagEt.setSelection(binding.hashtagSearchDialogHashtagEt.text.length)
                }
            }
        }
        binding.hashtagSearchDialogList.adapter = adapter

        /* 리스트 테스트 데이터*/
        // TODO: 현재 테스트 데이터임으로 API 연동 이후 삭제 요망
        for(i: Int in 0..10) {
            hashtagList.add(HashtagItem("해시태그_예시_$i"))
        }

        if(currentHashtag != null) {
            if(currentHashtag!!.endsWith(" ")) {
                currentHashtag = currentHashtag!!.substring(0, currentHashtag!!.length-1)
            }
            binding.hashtagSearchDialogHashtagEt.setText(currentHashtag)
            binding.hashtagSearchDialogHashtagEt.setSelection(currentHashtag!!.length)
        }

        /* 해시태그 입력 et 필드 */
        binding.hashtagSearchDialogHashtagEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            /* 해시태그 검색 문자열 */
            var text = ""
            /* 해시태그 검색 시작 여부 */
            var isHashtagSearchStart : Boolean = false
            override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(search!!.isNotEmpty()) {
                    if(search.last().toString().startsWith("#")) {
                        isHashtagSearchStart = true
                        hashtagStartIndex = search.lastIndex
                        binding.hashtagSearchDialogList.visibility = View.VISIBLE
                    }
                    if(search.last().toString().startsWith(" ")) {
                        isHashtagSearchStart = false
                    }
                    if(isHashtagSearchStart) {
                        if(search.last().toString() != "#") {
                            text = search.toString().substring(search.toString().lastIndexOf("#")+1, search.length)
                            adapter.getFilter().filter(text)
                        }
                    }
                    else {
                        text = ""
                        binding.hashtagSearchDialogList.visibility = View.GONE
                    }
                }
                else {
                    text = ""
                    binding.hashtagSearchDialogList.visibility = View.GONE
                }
            }

            override fun afterTextChanged(str: Editable?) {
            }

        })

        /* 확인 버튼 */
        binding.hashtagSearchDialogConfirmBtn.setOnClickListener {
            val hashtag = binding.hashtagSearchDialogHashtagEt.text.toString()
            if(isCorrectHashtag(hashtag)) {
                result.getResultList(resultHashtagList)
                dismissNow()
            }
        }

        return binding.root
    }

    private fun isCorrectHashtag(hashtag: String) : Boolean {
        if(hashtag.isNotEmpty()) {
            resultHashtagList = ArrayList()
            val strTokenizer = StringTokenizer(hashtag, "#", true)
            var s: String
            var isToken = false
            while(strTokenizer.hasMoreTokens()) {
                s = strTokenizer.nextToken()
                if(s == "#") {
                    isToken = true
                }
                if(isToken) {
                    if(s.startsWith(" ")) {
                        Toast.makeText(requireContext(), "# 뒤에 공백이 올 수 없습니다!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    else {
                        if(s != "#") {
                            if(s.endsWith(" ")) {
                                s = s.substring(0, s.length-1)
                            }
                            if(s.contains(" ")) {
                                Toast.makeText(requireContext(), "해시태그는 #을 입력해야 합니다!", Toast.LENGTH_SHORT).show()
                                return false
                            }
                            resultHashtagList.add(s)
                            isToken = false
                        }
                    }
                }
                else {
                    Toast.makeText(requireContext(), "해시태그는 #을 입력해야 합니다!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return true
    }

    class HashtagAdapter() : RecyclerView.Adapter<HashtagAdapter.ViewHolder>() {

        lateinit var filterHashtagList : ArrayList<HashtagItem>
        lateinit var unFilterHashtagList : ArrayList<HashtagItem>
        lateinit var onClickListener: HashtagOnClickListener

        constructor(unFilterHashtagList: ArrayList<HashtagItem>) : this() {
            this.unFilterHashtagList = unFilterHashtagList
            this.filterHashtagList = unFilterHashtagList
        }

        interface HashtagOnClickListener {
            fun onClick(hashtag: String)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ListItemHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.hashtag.text = StringBuilder(filterHashtagList[position].hashtag).insert(0, "#")

            holder.hashtag.setOnClickListener {
                onClickListener.onClick((it as TextView).text.toString())
            }
        }

        override fun getItemCount(): Int {
            return filterHashtagList.size
        }

        fun getFilter() : Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val search = constraint.toString()
                    if(search.isEmpty()) {
                        filterHashtagList = unFilterHashtagList
                    }
                    else {
                        val filteringList = ArrayList<HashtagItem>()
                        for(hashtagItem: HashtagItem in unFilterHashtagList) {
                            if(hashtagItem.hashtag.contains(search)) {
                                filteringList.add(hashtagItem)
                            }
                        }
                        filterHashtagList = filteringList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filterHashtagList
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    if(results != null) {
                        filterHashtagList = results.values as ArrayList<HashtagItem>
                        notifyDataSetChanged()
                    }
                }

            }
        }

        class ViewHolder(binding: ListItemHashtagBinding) : RecyclerView.ViewHolder(binding.root) {
            var hashtag: TextView = binding.hashtagName
        }
    }

    class HashtagItem(val hashtag: String)


    override fun onResume() {
        super.onResume()
        dialogFragmentResize(this@HashtagSearchDialog, 0.99, 0.5)
    }

    private fun dialogFragmentResize(dialogFragment: DialogFragment, width: Double, height: Double) {

        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }

    class HashtagListLinearLayout(context: Context) : LinearLayoutManager(context) {
        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }

}