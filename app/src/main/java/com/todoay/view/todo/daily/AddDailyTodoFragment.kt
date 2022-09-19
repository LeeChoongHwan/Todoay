package com.todoay.view.todo.daily

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.todoay.R
import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.api.domain.todo.daily.DailyTodoAPI
import com.todoay.api.domain.todo.daily.dto.request.CreateDailyTodoRequest
import com.todoay.api.domain.todo.daily.dto.request.ModifyDailyTodoRequest
import com.todoay.data.category.Category
import com.todoay.data.todo.daily.Alarm
import com.todoay.data.todo.daily.DailyInfo
import com.todoay.databinding.FragmentAddDailyTodoBinding
import com.todoay.global.util.PrintUtil
import com.todoay.view.global.TodoayAlertDialogFragment
import com.todoay.view.global.interfaces.CreateValueResult
import com.todoay.view.global.interfaces.ModifiedTodoResult
import com.todoay.view.todo.common.HashtagSearchDialog
import com.todoay.view.todo.common.TimeExistDialogFragment
import com.todoay.view.todo.daily.interfaces.AddDailyTodoCategorySettingDialogResult
import com.todoay.view.todo.daily.interfaces.ModifiedDailyData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddDailyTodoFragment(val date: LocalDate, val category : Category) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentAddDailyTodoBinding

    /** 공개여부 */
    var isPublic : Boolean = false
    /** 투두 */
    var todo : String = ""
    /** 투두 입력 여부 */
    var isTodo: Boolean = false
    /** 해시태그 */
    var hashtagList : List<Hashtag>? = null
    /** 해시태그 입력 여부 */
    var isHashtag : Boolean = false
    /** Target 시간 */
    var time : LocalDateTime? = null
    /** 알람 */
    var alarm : Alarm? = null

    var isModificationMode : Boolean = false
    lateinit var modifiedData : DailyInfo

    lateinit var createResult : CreateValueResult
    lateinit var modifiedResult : ModifiedTodoResult

    /** DailyTodo API 서비스 */
    private val service by lazy { DailyTodoAPI.getInstance() }

    /* 애니메이션 선언 */
    lateinit var slideFromRight : Animation
    lateinit var slideFromLeft : Animation
    lateinit var slideToRight : Animation
    lateinit var slideToLeft : Animation
    lateinit var flipFront : Animator
    lateinit var flipBack : Animator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddDailyTodoBinding.inflate(inflater, container, false)
        isCancelable = false

        initAnimation()

        if(isModificationMode) {
            isPublic = modifiedData.isPublic

            todo = modifiedData.todo
            binding.addDailyTodoTodoEt.setText(todo)
            isTodo = true

            hashtagList = modifiedData.hashtagList
            if(!hashtagList.isNullOrEmpty()) {
                val hashtagSb = StringBuilder()
                this.hashtagList!!.stream()
                    .map { h -> h.toString() }
                    .forEach(hashtagSb::append)
                binding.addDailyTodoHashtagEt.setText(hashtagSb)
                isHashtag = true
            }
            time = modifiedData.time
            if(time != null) setTimeContents(time!!)
            alarm = modifiedData.alarm
            if(alarm  != null) {
                setAlarmContents(alarm!!)
            }

            binding.addDailyTodoConfirmBtn.text = "수정하기"
            checkEnableConfirmButton(binding.addDailyTodoConfirmBtn)
        }

        /* 취소 버튼 */
        binding.addDailyTodoCancelBtn.setOnClickListener {
            dismiss()
        }

        /** /////////////////////////////////////////////////////////////////////////////////////////// */
        /** Main Layout */
        val mainLayout = binding.addDailyTodoMainLayout
        val detailsLayout = binding.addDailyTodoDetailsLayout

        /*
        공개여부 버튼
        default: private (선택 가능)
         */
        val publicButton = binding.addDailyTodoPublicBtn
        val privateButton = binding.addDailyTodoPrivateBtn
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

        /* 카테고리 버튼 */
        val categoryBtn = binding.addDailyTodoCategoryBtn
        setCategoryButton(category.name, category.color)
        categoryBtn.setOnClickListener {
            val categorySettingDialog = AddDailyTodoCategorySettingDialog()
            categorySettingDialog.show(parentFragmentManager, categorySettingDialog.tag)
            categorySettingDialog.result = object : AddDailyTodoCategorySettingDialogResult {
                override fun getCategory(category: Category) {
                    setCategoryButton(category.name, category.color)
                }
            }
        }

        /* 투두 입력 et */
        var todoEditText = binding.addDailyTodoTodoEt
        todoEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(todoEditText.text.toString() != "") {
                    isTodo = true
                    checkEnableConfirmButton(binding.addDailyTodoConfirmBtn)
                }
                else {
                    isTodo = false
                    checkEnableConfirmButton(binding.addDailyTodoConfirmBtn)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        /* 해시태그 입력 et */
        binding.addDailyTodoHashtagEt.setOnClickListener {
            val hashtagSearchDialog = HashtagSearchDialog()
            if(isHashtag) {
                hashtagSearchDialog.currentHashtag = binding.addDailyTodoHashtagEt.text.toString()
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
                        binding.addDailyTodoHashtagEt.setText(pHashtag)
                        isHashtag = true
                        if(!isPublic) {
                            changeLockButtonToPublicButton()
                        }
                    }
                    else {
                        hashtagList = null
                        binding.addDailyTodoHashtagEt.setText("")
                        isHashtag = false
                    }
                }
            }
        }

        /* 추가 정보 입력 버튼 */
        binding.addDailyTodoDetailsBtn.setOnClickListener {
            if(detailsLayout.visibility == View.INVISIBLE) {
                detailsLayout.startAnimation(slideFromRight)
                mainLayout.startAnimation(slideToLeft)
                detailsLayout.visibility = View.VISIBLE
                mainLayout.visibility = View.INVISIBLE
            }
        }

        /* 추가하기 or 수정하기 버튼 */
        binding.addDailyTodoConfirmBtn.setOnClickListener {
            if(isModificationMode) {
                modifyDailyTodo()
            } else {
                createDailyTodo()
            }
        }

        /** /////////////////////////////////////////////////////////////////////////////////////////// */
        /** Details Layout */
        /* 뒤로가기 버튼 */
        binding.addDailyTodoDetailsBackBtn.setOnClickListener {
            if(mainLayout.visibility == View.INVISIBLE) {
                mainLayout.startAnimation(slideFromLeft)
                detailsLayout.startAnimation(slideToRight)
                mainLayout.visibility = View.VISIBLE
                detailsLayout.visibility = View.INVISIBLE
            }
        }

        /* 시간설정 버튼 */
        binding.addDailyTodoDetailsTimeSettingBtn.setOnClickListener {
            settingTime()
        }

        /* 알람설정 버튼 */
        binding.addDailyTodoDetailsAlarmSettingBtn.setOnClickListener {
            settingAlarm()
        }

        return binding.root
    }

    private fun createDailyTodo() {
        val request = CreateDailyTodoRequest(
            todo = binding.addDailyTodoTodoEt.text.toString(),
            alarm = if (alarm != null) alarm!!.alarmTime else null,
            time = time,
            location = binding.addDailyTodoDetailsLocationEt.text.toString(),
            partner = binding.addDailyTodoDetailsPartnerEt.text.toString(),
            date = date,
            categoryId = category.id,
            hashtagList = hashtagList,
            isPublic = isPublic
        )
        service.createDailyTodo(
            request,
            onResponse = {
                createResult.isCreate(true)
                dismiss()
            },
            onErrorResponse = {

            },
            onFailure = {}
        )
    }

    private fun modifyDailyTodo() {
        val request = ModifyDailyTodoRequest(
            todo = binding.addDailyTodoTodoEt.text.toString(),
            alarm = this.alarm?.alarmTime,
            time = this.time,
            location = binding.addDailyTodoDetailsLocationEt.text.toString(),
            partner = binding.addDailyTodoDetailsPartnerEt.text.toString(),
            date = this.date,
            categoryId = this.category.id,
            hashtagList = this.hashtagList,
            isPublic = this.isPublic
        )
        service.modifyDailyTodo(
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

    private fun initAnimation() {
        slideFromRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_right)
        slideFromLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_left)
        slideToRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_to_right)
        slideToLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_to_left)
        flipFront = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_front)
        flipBack = AnimatorInflater.loadAnimator(requireContext(), R.anim.flip_back)
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
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_add_daily_todo_category_btn) as GradientDrawable
        drawable.setColor(Color.parseColor(color))
        binding.addDailyTodoCategoryBtn.setBackgroundDrawable(drawable)
        binding.addDailyTodoCategoryBtn.text = name
    }

    /**
     * 공개여부 버튼 변경.
     * 비공개 -> 공개.
     */
    private fun changeLockButtonToPublicButton() {
        flipFront.setTarget(binding.addDailyTodoPrivateBtn)
        flipBack.setTarget(binding.addDailyTodoPublicBtn)
        flipFront.start()
        flipBack.start()
        binding.addDailyTodoPrivateBtn.isClickable = false
        binding.addDailyTodoPublicBtn.isClickable = true
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
        flipFront.setTarget(binding.addDailyTodoPublicBtn)
        flipBack.setTarget(binding.addDailyTodoPrivateBtn)
        flipFront.start()
        flipBack.start()
        binding.addDailyTodoPublicBtn.isClickable = false
        binding.addDailyTodoPrivateBtn.isClickable = true
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
        binding.addDailyTodoDetailsTimeSettingBtn.text = "시간 설정"
        binding.addDailyTodoDetailsTimeSettingBtn.setTextColor(resources.getColor(R.color.gray))
        if(alarm != null)
            deleteAlarm()
    }

    /**
     * 시간 변수 초기화 및 버튼 텍스트 변경.
     */
    private fun setTimeContents(time: LocalDateTime) {
        this.time = time
        val printTime = PrintUtil.convertDateTimePrintFormat(this.time!!)
        binding.addDailyTodoDetailsTimeSettingBtn.text = printTime
        binding.addDailyTodoDetailsTimeSettingBtn.setTextColor(resources.getColor(R.color.main_color))
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
            override fun getAlarm(alarm : Alarm) {
                setAlarmContents(alarm)
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
        alarmSettingDialog.currentAlarm = alarm!!
        alarmSettingDialog.show(parentFragmentManager, alarmSettingDialog.tag)
        alarmSettingDialog.result = object : AlarmSettingDialog.AlarmSettingDialogResult {
            override fun getAlarm(alarm: Alarm) {
                setAlarmContents(alarm)
            }
        }
    }

    /**
     * 설정된 알람 삭제.
     */
    private fun deleteAlarm() {
        alarm = null
        binding.addDailyTodoDetailsAlarmSettingBtn.setImageResource(R.drawable.ic_alarm_default)
    }

    /**
     * 알람 변수 초기화 및 아이콘 변경.
     */
    private fun setAlarmContents(alarm: Alarm) {
        this.alarm = alarm
        binding.addDailyTodoDetailsAlarmSettingBtn.setImageResource(R.drawable.ic_alarm_exist)
    }


}