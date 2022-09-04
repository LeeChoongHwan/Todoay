package com.todoay.view.todo

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.bottomfragmenttest.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.databinding.FragmentAddCTodoBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddCTodoFragment(val date: LocalDate) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentAddCTodoBinding

    /**
     * Used Variable
     */
    /* 공개여부 */
    var isPublic : Boolean = false
    /* 카테고리 */
    var categoryName : String = ""
    var categoryColor : String = ""
    /* 투두 */
    var todo : String = ""
    var isTodo: Boolean = false
    /* 해시태그 */
    var hashtagList : List<Hashtag>? = null
    var isHashtag : Boolean = false
    /* Target 시간 */
    var time : LocalDateTime? = null
    /* 알람 */
    var alarm : LocalDateTime? = null
    var alarmName : String? = null
    /* 장소 */
    var location : String? = null
    /* 함께하는 사람 */
    var partner : String? = null

    /* 애니메이션 선언 */
    lateinit var slideFromRight : Animation
    lateinit var slideFromLeft : Animation
    lateinit var slideToRight : Animation
    lateinit var slideToLeft : Animation
    lateinit var flipFront : Animator
    lateinit var flipBack : Animator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCTodoBinding.inflate(inflater, container, false)
        isCancelable = false

        slideFromRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_right)
        slideFromLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_left)
        slideToRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_to_right)
        slideToLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_to_left)
        flipFront = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_front)
        flipBack = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_back)

        /* 취소 버튼 */
        binding.addCTodoCancelBtn.setOnClickListener {
            dismiss()
        }

        /** /////////////////////////////////////////////////////////////////////////////////////////// */
        /** Main Layout */
        val mainLayout = binding.addCTodoMainLayout
        val detailsLayout = binding.addCTodoDetailsLayout

        /*
        공개여부 버튼
        default: private (선택 가능)
         */
        val publicButton = binding.addCTodoPublicBtn
        val privateButton = binding.addCTodoPrivateBtn
        /* default 설정에 따른 초기 공개여부 버튼 설정 */
        if(isPublic) {
            publicButton.bringToFront()
        }
        else {
            privateButton.bringToFront()
        }
        /* 공개여부 버튼 클릭 시 애니메이션을 이용한 버튼 전환 */
        var scale = requireContext().resources.displayMetrics.density
        publicButton.cameraDistance = 8000 * scale
        privateButton.cameraDistance = 8000 * scale
        publicButton.setOnClickListener {
            changeLockButtonToPrivateButton()
        }
        privateButton.setOnClickListener {
            changeLockButtonToPublicButton()
        }

        /*
        카테고리 버튼
         */
        /* 디폴트 카테고리? -> 일반 */
        val categoryBtn = binding.addCTodoCategoryBtn
        categoryName = "일반"
        categoryColor = resources.getString(R.color.main_color)
        setCategoryButton(categoryName, categoryColor)
        categoryBtn.setOnClickListener {
            val categorySettingDialog = AddCTodoCategorySettingDialog()
            categorySettingDialog.show(parentFragmentManager, categorySettingDialog.tag)
            categorySettingDialog.result = object :
                AddCTodoCategorySettingDialog.AddCTodoCategorySettingDialogResult {
                override fun getCategory(category: AddCTodoCategorySettingDialog.CategorySettingItem) {
                    categoryName = category.name
                    categoryColor = categoryColor
                    setCategoryButton(categoryName, categoryColor)
                }
            }
        }

        /*
        투두 입력 et
         */
        var todoEditText = binding.addCTodoTodoEt
        todoEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(todoEditText.text.toString() != "") {
                    isTodo = true
                    checkEnableConfirmButton(binding.addCTodoConfirmBtn)
                }
                else {
                    isTodo = false
                    checkEnableConfirmButton(binding.addCTodoConfirmBtn)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        /*
        해시태그 입력 et
         */
        binding.addCTodoHashtagEt.setOnClickListener {
            val hashtagSearchDialog = HashtagSearchDialog()
            if(isHashtag) {
                hashtagSearchDialog.currentHashtag = binding.addCTodoHashtagEt.text.toString()
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
                        binding.addCTodoHashtagEt.setText(pHashtag)
                        isHashtag = true
                        if(!isPublic) {
                            changeLockButtonToPublicButton()
                        }
                    }
                    else {
                        hashtagList = null
                        binding.addCTodoHashtagEt.setText("")
                        isHashtag = false
                    }
                }
            }
        }

        /*
        추가 정보 입력 버튼
         */
        binding.addCTodoDetailsBtn.setOnClickListener {
            if(detailsLayout.visibility == View.INVISIBLE) {
                detailsLayout.startAnimation(slideFromRight)
                mainLayout.startAnimation(slideToLeft)
                detailsLayout.visibility = View.VISIBLE
                mainLayout.visibility = View.INVISIBLE
            }
        }

        /*
        추가하기 버튼
         */
        binding.addCTodoConfirmBtn.setOnClickListener {
            date
            isPublic
            categoryName
            todo = binding.addCTodoTodoEt.text.toString()
            hashtagList
            time
            alarm
            location = binding.addCTodoDetailsLocationEt.text.toString()
            partner = binding.addCTodoDetailsPartnerEt.text.toString()
        }

        /** /////////////////////////////////////////////////////////////////////////////////////////// */
        /** Details Layout */
        /*
        뒤로가기 버튼
         */
        binding.addCTodoDetailsBackBtn.setOnClickListener {
            if(mainLayout.visibility == View.INVISIBLE) {
                mainLayout.startAnimation(slideFromLeft)
                detailsLayout.startAnimation(slideToRight)
                mainLayout.visibility = View.VISIBLE
                detailsLayout.visibility = View.INVISIBLE
            }
        }

        /*
        시간설정 버튼
         */
        binding.addCTodoDetailsTimeSettingBtn.setOnClickListener {
            settingTime()
        }

        /*
        알람설정 버튼
        알람 존재시 다이얼로그 !
         */
        binding.addCTodoDetailsAlarmSettingBtn.setOnClickListener {
            settingAlarm()
        }

        /* 장소 입력 et */

        /* 함께하는 사람 입력 et */

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
     * 카테고리 버튼 변경
     */
    private fun setCategoryButton(name: String, color: String) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_add_c_todo_category_btn) as GradientDrawable
        drawable.setColor(Color.parseColor(color))
        binding.addCTodoCategoryBtn.setBackgroundDrawable(drawable)
        binding.addCTodoCategoryBtn.text = name
    }

    /**
     * 공개여부 버튼 변경.
     * 비공개 -> 공개.
     */
    private fun changeLockButtonToPublicButton() {
        flipFront.setTarget(binding.addCTodoPrivateBtn)
        flipBack.setTarget(binding.addCTodoPublicBtn)
        flipFront.start()
        flipBack.start()
        binding.addCTodoPrivateBtn.isClickable = false
        binding.addCTodoPublicBtn.isClickable = true
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
        flipFront.setTarget(binding.addCTodoPublicBtn)
        flipBack.setTarget(binding.addCTodoPrivateBtn)
        flipFront.start()
        flipBack.start()
        binding.addCTodoPublicBtn.isClickable = false
        binding.addCTodoPrivateBtn.isClickable = true
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

    /**
     * 시간설정 버튼 클릭에 따른 투두 시간 설정.
     */
    private fun settingTime() {
        if (time == null) setTimeIsNull()
        else setTimeIsExists()
    }

    /**
     * 설정된 시간이 없는 경우, 초기 시간 설정.
     */
    private fun setTimeIsNull() {
        val timePickerDialog = TimePickerDialogFragment(date)
        timePickerDialog.show(parentFragmentManager, timePickerDialog.tag)
        timePickerDialog.result = object : TimePickerDialogFragment.TimePickerDialogResult {
            override fun getTime(time: LocalDateTime) {
                setTimeContents(time)
            }
        }
    }

    /**
     * 설정된 시간이 있는 경우, 설정된 시간의 '수정' 또는 '삭제' 선택 요청.
     */
    private fun setTimeIsExists() {
        val timeExistDialog = TimeExistDialogFragment()
        timeExistDialog.show(parentFragmentManager, timeExistDialog.tag)
        timeExistDialog.result = object : TimeExistDialogFragment.TimeExistDialogResult {
            override fun getClick(click: TimeExistDialogFragment.TimeExistClick) {
                when (click) {
                    TimeExistDialogFragment.TimeExistClick.MODIFY -> {
                        Handler().run {
                            this.postDelayed({
                                modifyTime()
                            },50)
                        }
                    }
                    TimeExistDialogFragment.TimeExistClick.DELETE -> {
                        deleteTime()
                    }
                }
            }
        }
    }

    /**
     * 설정된 시간 수정.
     * TimePickerDialog에 현재설정시간을 전달.
     */
    private fun modifyTime() {
        val timePickerDialog = TimePickerDialogFragment(date)
        timePickerDialog.currentTime = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH).format(time)
        timePickerDialog.show(parentFragmentManager, timePickerDialog.tag)
        timePickerDialog.result = object : TimePickerDialogFragment.TimePickerDialogResult {
            override fun getTime(time: LocalDateTime) {
                setTimeContents(time)
            }
        }
    }

    /**
     * 설정된 시간 삭제.
     */
    private fun deleteTime() {
        time = null
        binding.addCTodoDetailsTimeSettingBtn.text = "시간 설정"
        binding.addCTodoDetailsTimeSettingBtn.setTextColor(resources.getColor(R.color.gray))
        if(alarm != null)
            deleteAlarm()
    }

    /**
     * 시간 변수 초기화 및 버튼 텍스트 변경.
     */
    private fun setTimeContents(time: LocalDateTime) {
        this.time = time
        val printTime = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH).format(this.time)
        binding.addCTodoDetailsTimeSettingBtn.text = printTime
        binding.addCTodoDetailsTimeSettingBtn.setTextColor(resources.getColor(R.color.main_color))
    }

    /**
     * 알람설정 버튼 클릭에 따른 투두 알람 설정.
     */
    private fun settingAlarm() {
        if (alarm == null) setAlarmIsNull()
        else setAlarmIsExists()
    }

    /**
     * 설정된 알람이 없는 경우, 초기 알람 설정.
     */
    private fun setAlarmIsNull() {
        if (time != null) setAlarmTimeIsExists()
        else setAlarmTimeIsNull()
    }

    /**
     * 설정된 알람이 있는 경우, 설정된 알람의 '수정' 또는 '삭제' 선택 요청.
     */
    private fun setAlarmIsExists() {
        val timeExistDialog = TimeExistDialogFragment()
        timeExistDialog.show(parentFragmentManager, timeExistDialog.tag)
        timeExistDialog.result = object : TimeExistDialogFragment.TimeExistDialogResult {
            override fun getClick(click: TimeExistDialogFragment.TimeExistClick) {
                when (click) {
                    TimeExistDialogFragment.TimeExistClick.MODIFY -> {
                        Handler().run {
                            this.postDelayed({
                                modifyAlarm()
                            }, 50)
                        }
                    }
                    TimeExistDialogFragment.TimeExistClick.DELETE -> {
                        deleteAlarm()
                    }
                }
            }
        }
    }

    /**
     * 초기 알람 설정.
     * 설정된 시간이 있는 경우.
     */
    private fun setAlarmTimeIsExists() {
        val alarmSettingDialog = AlarmSettingDialog(time!!)
        alarmSettingDialog.show(parentFragmentManager, alarmSettingDialog.tag)
        alarmSettingDialog.result = object : AlarmSettingDialog.AlarmSettingDialogResult {
            override fun getAlarm(_alarmTime: LocalDateTime, _alarmName: String) {
                setAlarmContents(_alarmTime, _alarmName)
            }
        }
    }


    /**
     * 초기 알람 설정.
     * 설정된 시간이 없는 경우, 시간 설정 요청 및 시간 설정.
     */
    private fun setAlarmTimeIsNull() {
        val alarmNotExistTimeDialog = TodoayAlertDialogFragment()
        alarmNotExistTimeDialog.message = "설정된 시간이 없습니다!\n시간을 설정하시겠어요?"
        alarmNotExistTimeDialog.show(parentFragmentManager, alarmNotExistTimeDialog.tag)
        alarmNotExistTimeDialog.result = object : TodoayAlertDialogFragment.AlertDialogResult {
            override fun getValue(isPositive: Boolean) {
                if (isPositive) {
                    setTimeForSettingAlarm()
                }
            }
        }
    }

    /**
     * 설정된 시간이 없음에 따라 알람 설정을 위한 시간 설정 후 알람 설정.
     */
    private fun setTimeForSettingAlarm() {
        val timePickerDialog = TimePickerDialogFragment(date)
        timePickerDialog.show(parentFragmentManager, timePickerDialog.tag)
        timePickerDialog.result = object : TimePickerDialogFragment.TimePickerDialogResult {
            override fun getTime(time: LocalDateTime) {
                setTimeContents(time)
                setAlarmTimeIsExists()
            }
        }
    }


    /**
     * 설정된 알람 수정.
     * AlarmSettingDialog에 현재설정알람을 전달.
     */
    private fun modifyAlarm() {
        val alarmSettingDialog = AlarmSettingDialog(time!!)
        alarmSettingDialog.currentAlarmName = alarmName!!
        alarmSettingDialog.show(parentFragmentManager, alarmSettingDialog.tag)
        alarmSettingDialog.result = object : AlarmSettingDialog.AlarmSettingDialogResult {
            override fun getAlarm(alarm: LocalDateTime, alarmName: String) {
                setAlarmContents(alarm, alarmName)
            }
        }
    }

    /**
     * 설정된 알람 삭제.
     */
    private fun deleteAlarm() {
        alarm = null
        alarmName = null
        binding.addCTodoDetailsAlarmSettingBtn.setImageResource(R.drawable.ic_alarm_default)
    }

    /**
     * 알람 변수 초기화 및 아이콘 변경.
     */
    private fun setAlarmContents(alarm: LocalDateTime, alarmName: String) {
        this.alarm = alarm
        this.alarmName = alarmName
        binding.addCTodoDetailsAlarmSettingBtn.setImageResource(R.drawable.ic_alarm_exist)
    }


}