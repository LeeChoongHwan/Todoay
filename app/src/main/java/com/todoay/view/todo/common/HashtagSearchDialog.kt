package com.todoay.view.todo.common

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.api.domain.hashtag.HashtagAPI
import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.databinding.FragmentHashtagSearchDialogBinding
import com.todoay.databinding.ListItemHashtagBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class HashtagSearchDialog : DialogFragment() {

    lateinit var binding : FragmentHashtagSearchDialogBinding
    lateinit var result : HashtagSearchDialogResult
    lateinit var resultHashtagList : ArrayList<Hashtag>
    lateinit var adapter: HashtagAdapter
    var currentHashtag: String? = null

    /** 해시태그 검색 리스트 */
    private var hashtagList: ArrayList<Hashtag> = ArrayList()
    /** 해시태그 검색 시작 인덱스 (해시태그 클릭 시 edittext의 텍스트 변경에 사용) */
    private var searchStartIndex = 0
    /** 해시태그 검색 문자열 */
    private var searchValue = ""
    /** 해시태그 검색 시작 여부 */
    private var isSearchStart : Boolean = false
    /** 해시태그 검색 시 다음 페이지 존재 여부 */
    private var hasNextPage by Delegates.notNull<Boolean>()
    /** 해시태그 검색 시 다음 페이지 넘버 */
    private var nextPageNum by Delegates.notNull<Int>()

    /** 해시태그 API 서비스 */
    private val service by lazy { HashtagAPI.getInstance() }

    // RxJava 디바운스 관련
    /** Observer 관련 메모리 누수 방지를 위해 fragment 종료 시 Observer를 종료하기 위한 객체 */
    private var disposable: Disposable? = null
    /** 검색 Observer 구독 객체 */
    private val searchObserver : PublishSubject<String> = PublishSubject.create()

    companion object {
        const val QUANTITY_FIVE = 5
        const val QUANTITY_SEVEN = 7
        const val QUANTITY_TEN = 10
    }

    interface HashtagSearchDialogResult {
        fun getResultList(hashtagResult: List<Hashtag>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.let{ disposable!!.dispose() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHashtagSearchDialogBinding.inflate(inflater, container, false)
//        isCancelable = false

        /* 검색 리스트 adapter 선언 및 지정 */
        binding.hashtagSearchDialogList.layoutManager = HashtagListLinearLayout(requireContext())
        adapter = HashtagAdapter(this)
        /* 검색 리스트 adapter 클릭 이벤트 */
        adapter.onClickListener = object : HashtagAdapter.HashtagOnClickListener {
            override fun onClick(hashtag: String) {
                StringBuilder(binding.hashtagSearchDialogHashtagEt.text.toString()).run {
                    this.delete(searchStartIndex, binding.hashtagSearchDialogHashtagEt.text.length)
                    this.insert(searchStartIndex, hashtag)
                    this.insert(this.length, " ")
                    binding.hashtagSearchDialogHashtagEt.setText(this)
                    binding.hashtagSearchDialogHashtagEt.setSelection(binding.hashtagSearchDialogHashtagEt.text.length)
                }
            }
        }
        binding.hashtagSearchDialogList.adapter = adapter

        /* Add 프래그먼트에서 이미 입력한 해시태그가 있는 경우, Dialog의 Edit text에도 추가 */
        if(currentHashtag != null) {
            if(currentHashtag!!.endsWith(" ")) {
                currentHashtag = currentHashtag!!.substring(0, currentHashtag!!.length-1)
            }
            binding.hashtagSearchDialogHashtagEt.setText(currentHashtag)
            binding.hashtagSearchDialogHashtagEt.setSelection(currentHashtag!!.length)
        }

        /* Initialize rxJava Subscribe */
        searchObserver.debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                adapter.getFilter().filter(it)
            }
            .subscribe()

        /* 해시태그 입력 et 필드 */
        binding.hashtagSearchDialogHashtagEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(search!!.isNotEmpty()) {
                    if(search.last().toString().startsWith("#")) {
                        isSearchStart = true
                        searchStartIndex = search.lastIndex
                    }
                    if(search.last().toString().startsWith(" ")) {
                        isSearchStart = false
                    }
                    adapter.isSearchStart = isSearchStart
                    /* 해시태그 검색 시작 */
                    if(isSearchStart) {
                        binding.hashtagSearchDialogList.visibility = View.VISIBLE
                        if(search.last().toString() != "#") {
                            searchValue = search.toString().substring(search.toString().lastIndexOf("#")+1, search.length)
                            searchObserver.onNext(searchValue)
                        }
                    }
                    /* 해시태그 검색 종료 */
                    else {
                        searchValue = ""
                        adapter.closeSearch()
                        binding.hashtagSearchDialogList.visibility = View.GONE
                        binding.hashtagSearchDialogViewMoreBtn.visibility = View.GONE
                    }
                }
                /* 필드가 Empty 일 경우 */
                else {
                    searchValue = ""
                    isSearchStart = false
                    adapter.closeSearch()
                    binding.hashtagSearchDialogList.visibility = View.GONE
                    binding.hashtagSearchDialogViewMoreBtn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(str: Editable?) {
                if(binding.hashtagSearchDialogHashtagEt.text.toString().isBlank()) {
                    binding.hashtagSearchDialogConfirmBtn.text = "취소"
                }
                else {
                    binding.hashtagSearchDialogConfirmBtn.text = "확인"
                }
            }
        })

        /* 해시태그 et 키보드 검색 버튼 */
        binding.hashtagSearchDialogHashtagEt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mainAct.hideKeyboard(binding.hashtagSearchDialogHashtagEt)
                    if(searchValue.isNotBlank()) {
                        service.getHashtag(
                            searchValue,
                            0,
                            QUANTITY_TEN,
                            onResponse = {
                                adapter.addHashtagList(it.hashtagList)
                                hasNextList(it.hasNext, it.nextPageNum)
                            },
                            onErrorResponse = {
                                if(it.code == "NO_MORE_DATA") {
                                    hasNextList(false, 0)
                                }
                            },
                            onFailure = {}
                        )
                    }
                    return true
                }
                return false
            }
        })

        /* 해시태그 리스트 스크롤 */
        binding.hashtagSearchDialogList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && hasNextPage) {
                    service.getHashtag(
                        searchValue,
                        nextPageNum,
                        QUANTITY_FIVE,
                        onResponse = {
                            adapter.addHashtagList(it.hashtagList)
                            hasNextList(it.hasNext, it.nextPageNum)
                        },
                        onErrorResponse = {
                            if(it.code == "NO_MORE_DATA") {
                                hasNextList(false, 0)
                            }
                        },
                        onFailure = {}
                    )
                }
            }
        })

        /* 더보기 버튼 */
        binding.hashtagSearchDialogViewMoreBtn.setOnClickListener {
            mainAct.hideKeyboard(binding.hashtagSearchDialogHashtagEt)
            if(hasNextPage){
                service.getHashtag(
                    searchValue,
                    nextPageNum,
                    QUANTITY_SEVEN,
                    onResponse = {
                        adapter.addHashtagList(it.hashtagList)
                        hasNextList(it.hasNext, it.nextPageNum)
                    },
                    onErrorResponse = {
                        if(it.code == "NO_MORE_DATA") {
                            hasNextList(false, 0)
                        }
                    },
                    onFailure = {}
                )
            }
        }

        /* 확인 버튼 */
        binding.hashtagSearchDialogConfirmBtn.setOnClickListener {
            val hashtag = binding.hashtagSearchDialogHashtagEt.text.toString()
            if(verifyHashtag(hashtag)) {
                result.getResultList(resultHashtagList)
                dismissNow()
            }
        }

        return binding.root
    }

    /**
     * Hashtag 입력 값의 검증을 수행하는 메소드.
     * 확인 버튼 클릭 시, 즉 해시태그 입력을 종료할 시 입력 값에 대한 검증을 실시한다.
     * 검증에 성공한 경우, 해시태그 리스트에 HashtagDto로 변환한 입력 값을 추가한 후, true를 리턴한다.
     * 검증에 실패한 경우, false를 리턴하며, 검증에 실패한 케이스는 다음과 같다.
     * 1. # 바로 뒤에 공백이 오는 경우
     * 2. 해시태그 입력값 앞에 #이 없는 경우
     *
     * @param hashtag 해시태그 입력 Edit text's String value
     * @return validation boolean value
     */
    private fun verifyHashtag(hashtag: String) : Boolean {
        resultHashtagList = ArrayList()
        if(hashtag.isNotEmpty()) {
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
                        mainAct.showShortToast("# 뒤에 공백이 올 수 없습니다!")
                        return false
                    }
                    else {
                        if(s != "#") {
                            if(s.endsWith(" ")) {
                                s = s.substring(0, s.length-1)
                            }
                            if(s.contains(" ")) {
                                mainAct.showShortToast("해시태그는 #을 입력해야 합니다!")
                                return false
                            }
                            resultHashtagList.add(Hashtag(s))
                            isToken = false
                        }
                    }
                }
                else {
                    mainAct.showShortToast("해시태그는 #을 입력해야 합니다!")
                    return false
                }
            }
        }
        return true
    }

    /**
     * 해시태그 검색 시 보여지는 해시태그 이외의 다른 해시태그 리스트의 여부를
     * 전달 받아 '더보기' 텍스트를 표현하는 메소드.
     *
     * @param hasNext 다음 페이지가 존재하는지에 대한 Boolean 변수
     * @param nextPageNum 다음 페이지 number
     */
    fun hasNextList(hasNext : Boolean, nextPageNum : Int) {
        this.hasNextPage = hasNext
        this.nextPageNum = nextPageNum
        if(this.hasNextPage) {
            binding.hashtagSearchDialogViewMoreBtn.visibility = View.VISIBLE
        }
        else {
            binding.hashtagSearchDialogViewMoreBtn.visibility = View.GONE
        }
    }

    class HashtagAdapter() : RecyclerView.Adapter<HashtagAdapter.ViewHolder>() {

        interface HashtagOnClickListener {
            fun onClick(hashtag: String)
        }

        lateinit var filterHashtagList : ArrayList<Hashtag>
        lateinit var onClickListener: HashtagOnClickListener
        lateinit var service : HashtagAPI
        lateinit var parent : HashtagSearchDialog

        var isSearchStart : Boolean = false

        constructor(parent: HashtagSearchDialog) : this() {
            this.parent = parent
            this.filterHashtagList = parent.hashtagList
            this.service = parent.service
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ListItemHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.hashtag.text = StringBuilder(filterHashtagList[position].name).insert(0, "#")

            holder.hashtag.setOnClickListener {
                onClickListener.onClick((it as TextView).text.toString())
            }
        }

        override fun getItemCount(): Int {
            return filterHashtagList.size
        }

        fun addHashtagList(additionalList : List<Hashtag>) {
            additionalList.stream()
                .filter { h ->
                    filterHashtagList.stream().noneMatch{ t -> h.equals(t) }
                }
                .forEach(filterHashtagList::add)
            notifyDataSetChanged()
        }

        fun clearList() {
            filterHashtagList.clear()
            notifyDataSetChanged()
        }

        fun closeSearch() {
            clearList()
            isSearchStart = false
        }

        fun getFilter() : Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val search = constraint.toString()
                    if(search.isNotEmpty() && isSearchStart) {
                        /* 해시태그 검색 (다음 페이지 존재 여부 확인) */
                        service.getHashtag(
                            search,
                            0,
                            QUANTITY_SEVEN,
                            onResponse = {
                                parent.hasNextList(it.hasNext, it.nextPageNum)
                                filterHashtagList = it.hashtagList as ArrayList<Hashtag>
                                notifyDataSetChanged()
                            },
                            onErrorResponse = {
                                if(it.code == "NO_MORE_DATA") {
                                    parent.hasNextList(false, 0)
                                }
                            },
                            onFailure = {}
                        )
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filterHashtagList
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    if(results != null) {
                        filterHashtagList = results.values as ArrayList<Hashtag>
                    }
                }

            }
        }

        class ViewHolder(binding: ListItemHashtagBinding) : RecyclerView.ViewHolder(binding.root) {
            var hashtag: TextView = binding.hashtagName
        }
    }

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