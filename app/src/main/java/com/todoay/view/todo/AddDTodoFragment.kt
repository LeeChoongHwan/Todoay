package com.example.bottomfragmenttest

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
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.databinding.FragmentAddDTodoBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AddDTodoFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentAddDTodoBinding

    /* variable */
    /* 공개여부 */
    var isPublic : Boolean = false
    /* 투두 */
    var todo : String = ""
    var isTodo : Boolean = false
    /* 투두 날짜 */
    var dueDate : LocalDate? = null
    /* 우선순위 */
    var priority : Int? = null
    /* 해시태그 */
    var hashtagList : List<String>? = null
    var isHashtag : Boolean = false
    /* 투두 설명 */
    var description : String? = null

    /* 애니메이션 선언 */
    lateinit var  flipFront : Animator
    lateinit var  flipBack : Animator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddDTodoBinding.inflate(inflater, container, false)
        isCancelable = false

        flipFront = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_front)
        flipBack = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_back)

        /* 취소 버튼 */
        binding.addDTodoCancelBtn.setOnClickListener {
            dismiss()
        }

        /*
        공개여부 버튼
        default: private (선택 가능)
         */
        val publicButton = binding.addDTodoPublicBtn
        val privateButton = binding.addDTodoPrivateBtn
        if(isPublic) {
            publicButton.bringToFront()
        }
        else {
            privateButton.bringToFront()
        }
        var scale = requireContext().resources.displayMetrics.density
        binding.addDTodoPrivateBtn.cameraDistance = 8000 * scale
        binding.addDTodoPublicBtn.cameraDistance = 8000 * scale
        publicButton.setOnClickListener {
            changeLockButtonToPrivateButton()
        }
        privateButton.setOnClickListener {
            changeLockButtonToPublicButton()
        }

        /* 투두 입력 et */
        var todoEditText = binding.addDTodoTodoEt
        todoEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(todoEditText.text.toString() != "") {
                    isTodo = true
                    checkEnableConfirmButton(binding.addDTodoConfirmBtn)
                }
                else {
                    isTodo = false
                    checkEnableConfirmButton(binding.addDTodoConfirmBtn)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        /* 날짜 입력 */
        binding.addDTodoDateSelectTv.text = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        binding.addDTodoCalendarBtn.setOnClickListener {
            val datePickerFragment = DatePickerDialogFragment(LocalDate.now())
            datePickerFragment.show(parentFragmentManager, datePickerFragment.tag)
            datePickerFragment.result = object: DatePickerDialogFragment.DatePickerResult {
                override fun getDate(date: LocalDate) {
                    dueDate = date
                    val printDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                    binding.addDTodoDateSelectTv.text = printDate
                }
            }
        }

        /* 우선순위 선택 */
        when(binding.addDTodoPriorityGroup.checkedRadioButtonId) {
            R.id.add_d_todo_priority_high_btn -> {
                priority = 0
            }
            R.id.add_d_todo_priority_mid_btn -> {
                priority = 1
            }
            R.id.add_d_todo_priority_low_btn -> {
                priority = 2
            }
        }
        binding.addDTodoPriorityGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id) {
                R.id.add_d_todo_priority_high_btn -> {
                    priority = 0
                }
                R.id.add_d_todo_priority_mid_btn -> {
                    priority = 1
                }
                R.id.add_d_todo_priority_low_btn -> {
                    priority = 2
                }
            }
        }

        /* 해시태그 입력 et */
        binding.addDTodoHashtagEt.setOnClickListener {
            val hashtagSearchDialog = HashtagSearchDialog()
            if(isHashtag) {
                hashtagSearchDialog.currentHashtag = binding.addDTodoHashtagEt.text.toString()
            }
            hashtagSearchDialog.show(parentFragmentManager, hashtagSearchDialog.tag)
            hashtagSearchDialog.result = object : HashtagSearchDialog.HashtagSearchDialogResult {
                override fun getResultList(hashtagResult: List<String>) {
                    if(hashtagResult.isNotEmpty()) {
                        hashtagList = hashtagResult
                        val pHashtag = StringBuilder()
                        hashtagList!!.forEach {
                            pHashtag.append("#$it ")
                        }
                        binding.addDTodoHashtagEt.setText(pHashtag)
                        isHashtag = true
                        if(!isPublic) {
                            changeLockButtonToPublicButton()
                        }
                    }
                    else {
                        hashtagList = null
                        binding.addDTodoHashtagEt.setText("")
                        isHashtag = false
                    }

                }
            }
        }

        /* 투두 설명 et */

        /* 추가하기 버튼 */
        binding.addDTodoConfirmBtn.setOnClickListener {

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
        다이얼로그 radius 및 height 지정
         */
        val resources = resources
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert(view != null)
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(40, 0, 40, 0)
            parent.layoutParams = layoutParams
        }
    }

    /**
     * 공개여부 버튼 변경.
     * 비공개 -> 공개.
     */
    private fun changeLockButtonToPublicButton() {
        flipFront.setTarget(binding.addDTodoPrivateBtn)
        flipBack.setTarget(binding.addDTodoPublicBtn)
        flipFront.start()
        flipBack.start()
        binding.addDTodoPrivateBtn.isClickable = false
        binding.addDTodoPublicBtn.isClickable = true
        isPublic = true
    }

    /**
     * 공개여부 버튼 변경.
     * 공개 -> 비공개.
     */
    private fun changeLockButtonToPrivateButton() {
        if(isHashtag) {
            Toast.makeText(requireContext(), "해시태그를 입력하시면 비공개로 전환할 수 없습니다!", Toast.LENGTH_SHORT).show()
            return
        }
        flipFront.setTarget(binding.addDTodoPublicBtn)
        flipBack.setTarget(binding.addDTodoPrivateBtn)
        flipFront.start()
        flipBack.start()
        binding.addDTodoPublicBtn.isClickable = false
        binding.addDTodoPrivateBtn.isClickable = true
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

}