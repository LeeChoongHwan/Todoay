package com.todoay.view.todo.dueDate

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.MainActivity.Companion.mainAct
import com.todoay.R
import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.api.domain.todo.dueDate.DueDateTodoAPI
import com.todoay.api.domain.todo.dueDate.dto.request.CreateDueDateTodoRequest
import com.todoay.api.domain.todo.dueDate.dto.request.ModifyDueDateTodoRequest
import com.todoay.data.todo.dueDate.DueDateInfo
import com.todoay.databinding.FragmentAddDueDateTodoBinding
import com.todoay.view.todo.common.DatePickerDialogFragment
import com.todoay.view.todo.common.HashtagSearchDialog
import com.todoay.view.global.interfaces.CreateValueResult
import com.todoay.view.global.interfaces.ModifiedTodoResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddDueDateTodoFragment : BottomSheetDialogFragment() {

    /** 바인딩 */
    lateinit var binding : FragmentAddDueDateTodoBinding

    /* variable */
    /** 공개여부 */
    var isPublic : Boolean = false
    /** 투두 */
    var todo : String = ""
    /** 투두 입력 여부 */
    var isTodo : Boolean = false
    /** 투두 날짜 */
    var dueDate : LocalDate = LocalDate.now()
    /** 우선순위 */
    lateinit var priority : String
    /** 해시태그 */
    var hashtagList : List<Hashtag>? = null
    /** 해시태그 입력 여부 */
    var isHashtag : Boolean = false

    /**
     * 수정 모드 여부
     * 이 변수는 해당 프래그먼트가 수정 모드로 호출되었는지 확인하는 변수이다.
     * 또한, 이 변수는 해당 프래그먼트이 Default가 추가(Add) 모드로서 false로 초기화한다.
     * 하지만, 해당 프래그먼트의 객체가 생성될 떄 이 변수의 값을 true로 초기화해줌으로
     * 해당 프래그먼트가 수정 모드로 호출되었는지 확인한다.
     */
    var isModificationMode : Boolean = false
    /**
     * 수정할 DueDateInfo 객체 데이터
     * isModificationMode를 통해 수정 모드임을 확인하면 이 객체 변수에 담겨있는
     * 정보들로 프래그먼트의 View에 초기화해준다.
     */
    lateinit var modifiedData : DueDateInfo

    /** Return Create Value Interface variable */
    lateinit var createResult : CreateValueResult
    /** Return Modified Value Interface variable */
    lateinit var modifiedResult : ModifiedTodoResult

    /** DueDateTodo API 서비스 */
    private val service by lazy { DueDateTodoAPI.getInstance() }

    /* 애니메이션 선언 */
    lateinit var  flipFront : Animator
    lateinit var  flipBack : Animator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddDueDateTodoBinding.inflate(inflater, container, false)
        isCancelable = false

        flipFront = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_front)
        flipBack = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_back)

        if(isModificationMode) {
            this.isPublic = modifiedData.isPublic

            this.todo = modifiedData.todo
            binding.addDueDateTodoTodoEt.setText(this.todo)
            this.isTodo = true

            this.dueDate = modifiedData.dueDate
            val printDate = this.dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            binding.addDueDateTodoDateSelectTv.text = printDate

            this.priority = modifiedData.priority
            when(this.priority) {
                "HIGH" -> binding.addDueDateTodoPriorityHighBtn.isChecked = true
                "MIDDLE" -> binding.addDueDateTodoPriorityMidBtn.isChecked = true
                "LOW" -> binding.addDueDateTodoPriorityLowBtn.isChecked = true
            }

            this.hashtagList = modifiedData.hashtagList
            if(!this.hashtagList.isNullOrEmpty()) {
                val hashtagSb = StringBuilder()
                this.hashtagList!!.stream()
                    .map { h -> "#${h.name} " }
                    .forEach(hashtagSb::append)
                binding.addDueDateTodoHashtagEt.setText(hashtagSb)
                isHashtag = true
            }

            binding.addDueDateTodoDescriptionEt.setText(modifiedData.description)

            binding.addDueDateTodoConfirmBtn.text = "수정하기"

        }

        /* 취소 버튼 */
        binding.addDueDateTodoCancelBtn.setOnClickListener {
            dismiss()
        }

        /*
        공개여부 버튼
        default: private (선택 가능)
         */
        val publicButton = binding.addDueDateTodoPublicBtn
        val privateButton = binding.addDueDateTodoPrivateBtn
        if(isPublic) { publicButton.bringToFront() }
        else { privateButton.bringToFront() }
        var scale = requireContext().resources.displayMetrics.density
        binding.addDueDateTodoPrivateBtn.cameraDistance = 8000 * scale
        binding.addDueDateTodoPublicBtn.cameraDistance = 8000 * scale
        publicButton.setOnClickListener {
            changeLockButtonToPrivateButton()
        }
        privateButton.setOnClickListener {
            changeLockButtonToPublicButton()
        }

        /* 투두 입력 et */
        var todoEditText = binding.addDueDateTodoTodoEt
        todoEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(todoEditText.text.toString() != "") {
                    isTodo = true
                    todo = todoEditText.text.toString()
                    checkEnableConfirmButton(binding.addDueDateTodoConfirmBtn)
                }
                else {
                    isTodo = false
                    todo = ""
                    checkEnableConfirmButton(binding.addDueDateTodoConfirmBtn)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        /* 날짜 입력 */
        binding.addDueDateTodoDateSelectTv.text = dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        binding.addDueDateTodoCalendarBtn.setOnClickListener {
            val datePickerFragment = DatePickerDialogFragment(LocalDate.now())
            datePickerFragment.show(parentFragmentManager, datePickerFragment.tag)
            datePickerFragment.result = object: DatePickerDialogFragment.DatePickerResult {
                override fun getDate(date: LocalDate) {
                    dueDate = date
                    val printDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                    binding.addDueDateTodoDateSelectTv.text = printDate
                }
            }
        }

        /* 우선순위 선택 */
        when(binding.addDueDateTodoPriorityGroup.checkedRadioButtonId) {
            R.id.add_due_date_todo_priority_high_btn -> priority = "HIGH"
            R.id.add_due_date_todo_priority_mid_btn -> priority = "MIDDLE"
            R.id.add_due_date_todo_priority_low_btn -> priority = "LOW"

        }
        binding.addDueDateTodoPriorityGroup.setOnCheckedChangeListener { _, id ->
            when(id) {
                R.id.add_due_date_todo_priority_high_btn -> priority = "HIGH"
                R.id.add_due_date_todo_priority_mid_btn -> priority = "MIDDLE"
                R.id.add_due_date_todo_priority_low_btn -> priority = "LOW"
            }
        }

        /* 해시태그 입력 et */
        binding.addDueDateTodoHashtagEt.setOnClickListener {
            val hashtagSearchDialog = HashtagSearchDialog()
            if(isHashtag) {
                hashtagSearchDialog.currentHashtag = binding.addDueDateTodoHashtagEt.text.toString()
            }
            hashtagSearchDialog.show(parentFragmentManager, hashtagSearchDialog.tag)
            hashtagSearchDialog.result = object : HashtagSearchDialog.HashtagSearchDialogResult {
                override fun getResultList(hashtagResult: List<Hashtag>) {
                    if(hashtagResult.isNotEmpty()) {
                        hashtagList = hashtagResult
                        val pHashtag = StringBuilder()
                        hashtagList!!.forEach {
                            pHashtag.append("#${it.name} ")
                        }
                        binding.addDueDateTodoHashtagEt.setText(pHashtag)
                        isHashtag = true
                        if(!isPublic) {
                            changeLockButtonToPublicButton()
                        }
                    }
                    else {
                        hashtagList = null
                        binding.addDueDateTodoHashtagEt.setText("")
                        isHashtag = false
                    }
                }
            }
        }

        /* 추가하기 버튼 */
        binding.addDueDateTodoConfirmBtn.setOnClickListener {
            if(isModificationMode) {
                modifyDueDateTodo()
            } else {
                createDueDateTodo()
            }
        }

        return binding.root
    }

    /**
     * DueDateTodo 생성 API 호출
     */
    private fun createDueDateTodo() {
        val request = CreateDueDateTodoRequest(
            todo = this.todo,
            description = binding.addDueDateTodoDescriptionEt.text.toString().ifEmpty { "" },
            isPublic = this.isPublic,
            dueDate = this.dueDate,
            priority = this.priority,
            hashtagList = if (this.hashtagList.isNullOrEmpty()) null else this.hashtagList
        )
        service.createDueDateTodo(
            request,
            onResponse = {
                createResult.isCreate(true)
                dismiss()
            },
            onErrorResponse = {
            },
            onFailure = {
                dismiss()
            }
        )
    }

    /**
     * DueDateTodo 수정 API 호출
     *
     */
    private fun modifyDueDateTodo() {
        val request = ModifyDueDateTodoRequest(
            todo = this.todo,
            description = binding.addDueDateTodoDescriptionEt.text.toString().ifEmpty { "" },
            isPublic = this.isPublic,
            dueDate = this.dueDate,
            priority = this.priority,
            hashtagList = if (this.hashtagList.isNullOrEmpty()) null else this.hashtagList
        )
        service.modifyDueDateTodo(
            modifiedData.id,
            request,
            onResponse = {
                modifiedResult.isModified(true, modifiedData.id)
                dismiss()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

    /**
     * 공개여부 버튼 변경.
     * 비공개 -> 공개.
     */
    private fun changeLockButtonToPublicButton() {
        flipFront.setTarget(binding.addDueDateTodoPrivateBtn)
        flipBack.setTarget(binding.addDueDateTodoPublicBtn)
        flipFront.start()
        flipBack.start()
        binding.addDueDateTodoPrivateBtn.isClickable = false
        binding.addDueDateTodoPublicBtn.isClickable = true
        isPublic = true
    }

    /**
     * 공개여부 버튼 변경.
     * 공개 -> 비공개.
     */
    private fun changeLockButtonToPrivateButton() {
        if(isHashtag) {
            mainAct.showShortToast("해시태그를 입력하시면 비공개로 전환할 수 없습니다!")
            return
        }
        flipFront.setTarget(binding.addDueDateTodoPublicBtn)
        flipBack.setTarget(binding.addDueDateTodoPrivateBtn)
        flipFront.start()
        flipBack.start()
        binding.addDueDateTodoPublicBtn.isClickable = false
        binding.addDueDateTodoPrivateBtn.isClickable = true
        isPublic = false
    }

    /**
     * 추가하기 버튼 변경.
     * 투두 필드를 입력해야 enable.
     */
    private fun checkEnableConfirmButton(confirmButton: Button) {
        if(isTodo) {
            confirmButton.isEnabled = true
            confirmButton.setBackgroundResource(R.drawable.bg_primary_btn)
        }
        else {
            confirmButton.isEnabled = false
            confirmButton.setBackgroundResource(R.drawable.bg_primary_fail_btn)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* 다이얼로그 radius 및 height 지정 */
        val resources = resources
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert(view != null)
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(40, 0, 40, 0)
            parent.layoutParams = layoutParams
        }
    }

}