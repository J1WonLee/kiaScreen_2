package com.copy.kiascreen.mypage

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatSpinner
import com.copy.kiascreen.login.LoginActivity
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.ActivityUserInfoBinding
import com.copy.kiascreen.mypage.vm.UserInfoViewModel
import com.copy.kiascreen.repeatOnStarted
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.AlertUtil
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

// 사용자 정보 열람 및 수정
@AndroidEntryPoint
class UserInfoActivity : BaseActivity<ActivityUserInfoBinding, UserInfoViewModel>(TransitionMode.HORIZON) {

    private lateinit var mailEditText : EditText
    private lateinit var mailIdEditText : EditText
    private lateinit var mailSpinner : AppCompatSpinner
    private lateinit var pwdEditText : EditText
    private lateinit var pwdChkEditText : EditText
    private lateinit var pwdChkText : TextView

    override val viewModel : UserInfoViewModel by viewModels()

    // 메일 첫글자 영문만
    private val mailIdFilter by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    // 메일 아이디 첫글자 이후
    private val mailIdFilter2 by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z\\d]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    private val mailDomainFilter by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z.]*$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    // 수정 완료시 메일 검증 정규식
    private val mailRegx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"

    private var isSignEnabled = false

//    private var user : User? = null

    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivityUserInfoBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        writeUserInfo()
        setPwdInputFilter()
        setKeyDownListener()
        setSpinnerAdapter()

        setRegisterBtn()
        setOnKeyListener()

        repeatOnStarted {
            viewModel.userInfoSharedFlow.collect {
                handleUpdateResult(it)
            }
        }
    }

    override fun getDataFromIntent() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        }
        else {
            intent.getParcelableExtra<User>("user")
        }
        Log.d("myPageTest", "user id = ${user?.id}")
    }


    override fun initView() {
        mailIdEditText = binding.emailIdEd
        mailEditText = binding.emailEd
        mailSpinner = binding.mailSpinner
        pwdEditText = binding.pwdEt
        pwdChkEditText = binding.pwdChkEt
        pwdChkText = binding.pwdChkHintTv
    }



    // 회원 정보를 뷰에 노출시킴
    private fun writeUserInfo() {
        binding.nameEt.text = user?.name
        binding.idEt.text = user?.id
        Log.d("userInfoTest", "${user?.mail}")
        try {
            var mailName = user?.mail!!.substring(0, user?.mail!!.indexOf('@'))

            Log.d("userInfoTest", "mailName = ${mailName}")

            var mailDomain = user?.mail!!.substring(user?.mail!!.indexOf('@') + 1)

            Log.d("userInfoTest", "mailDomain = ${mailDomain}")

            mailIdEditText.setText(mailName)
            mailEditText.setText(mailDomain)
        }
        catch (e : Exception ){
            Log.d("userinfoTest", "${user?.mail}")
        }
    }

    // 비밀번호 입력 타입
    private fun setPwdInputFilter() {
        pwdEditText.filters = arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                //val ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$") //영문 숫자 한글
                //영문 숫자 한글 천지인 middle dot[ᆞ]
                val ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*]*$");
                if (!ps.matcher(src).matches()) {
                    return@InputFilter ""
                } else {
                    return@InputFilter null
                }
            }
        )
    }

    private fun chkPwd() {
        if (pwdEditText.text.toString().equals(pwdChkEditText.text.toString())) {
            pwdChkText.text = "비밀번호가 일치합니다"
            pwdChkText.setTextColor(resources.getColor(R.color.blue))
            isSignEnabled = true
        }
        else {
            pwdChkText.text = "비밀번호가 일치하지 않습니다"
            pwdChkText.setTextColor(resources.getColor(R.color.red))
            isSignEnabled = false
        }
    }

    // 비밀번호, 메일 keydown 리스너
    private fun setKeyDownListener() {
        mailIdEditText.filters = arrayOf(mailIdFilter)

        pwdChkEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                chkPwd()
            }

            override fun afterTextChanged(s: Editable?) {
                chkPwd()
            }
        })

        // 이메일 아이디 입력 필터 첫글자 영문, 후에는 영문 + 숫자
        mailIdEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0 && mailIdEditText.text!!.isEmpty()) {
                    mailIdEditText.filters = arrayOf(mailIdFilter)
                }
                else if (count >= 1) {
                    mailIdEditText.filters = arrayOf(mailIdFilter2)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        mailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mailEditText.text.isNotEmpty()) {
                    mailEditText.filters = arrayOf(mailDomainFilter)
                }

            }

            override fun afterTextChanged(s: Editable?) { }

        })
    }

    // 스피너 어뎁터
    private fun setSpinnerAdapter() {
        var mAdapter = ArrayAdapter.createFromResource(this, R.array.email_domain_array , R.layout.item_spinner)
        mAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.mailSpinner.adapter = mAdapter

        binding.mailSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var mail = ""
            var cnt = 0
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("spinnerTest", "position = $position")
                when(position) {
                    0 -> {
                    }
                    1 -> {
                        mail = "naver.com"
                    }
                    2 -> {
                        mail = "nate.com"
                    }
                    3 -> {
                        mail = "goggle.com"
                    }
                    4 -> {
                        mail="daum.net"
                    }
                }

                // 선택한 메일을 editText에 입력해줌, 전에 작성한거 있으면 지워준다
                if (mailEditText.text.isNotEmpty() && cnt > 0) {
                    mailEditText.text.clear()
                    mailEditText.setText(mail)
                }
                else if (mailEditText.text.isEmpty() && cnt > 0) {
                    mailEditText.setText(mail)
                }
                cnt++
                mail=""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setRegisterBtn() {
        binding.editBtn.setOnClickListener {
            if (isMailChked() && isSignEnabled) {
                // 회원 수정 -> user객체로 교체 시켜준다 가능 , 비밀번호 변경 함
                var mail = mailIdEditText.text.toString()+"@"+mailEditText.text.toString()
                mail.trim()
                val user = User(binding.idEt.text.toString(), pwdEditText.text.toString(), user!!.name , mail)
                viewModel.updateUser(user)
            }
            else if (isMailChked() && !isSignEnabled) {
                // 메일만 변경한 경우
                var mail = mailIdEditText.text.toString()+"@"+mailEditText.text.toString()
                mail.trim()

                val user = User(binding.idEt.text.toString(), user?.pwd!! , user!!.name ,mail)
                viewModel.updateUser(user)
            }
            else {
                if (!isMailChked()) {
                    // 이메일 다시 입력
                    AlertUtil.makeAlertDialog(this, "이메일 입력을 확인해 주세요")
                }
                else {
                    // 비밀번호 확인 다름
                    AlertUtil.makeAlertDialog(this, "비밀번호가 일치하지 않습니다")
                }
            }
        }
    }

    private fun isMailChked() : Boolean {
        var mail =mailIdEditText.text.toString()+"@"+mailEditText.text.toString()
        val pattern = Pattern.compile(mailRegx)
        return if (mail.isNullOrEmpty() || mail.isNullOrBlank()) {
            false
        } else {
            val matcher = pattern.matcher(mail)
            Log.d("regitTest", "${matcher.matches()}")
            matcher.matches()
        }
    }

    private fun setOnKeyListener() {
        pwdChkEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (isSignEnabled) {
                    Log.d("userInfoTest", "isSignEnabled emailIdEd requestFocus!!")
                }
                true
            }
            false
        }

        mailIdEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d("userInfoTest", "mailIdEditText setOnKeyListener !!")
                mailSpinner.requestFocus()
                mailSpinner.isFocusable = true
                mailSpinner.performClick()
                true
            }
            false
        }
    }

    private fun handleUpdateResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("loginTest", "onLoading!!")
            is RoomUtil.Success -> {
                if (result.successOrNull() != null) {
                    var result = result.data as Int

                    if (result == 1) {
//                        AlertUtil.makeAlertDialogLoginIntent(this, "회원 정보가 수정되었습니다" )
                        AlertUtil.makeAlertDialogLoginIntent(this, "회원정보가 수정되었습니다", LoginActivity::class.java)
                    }

                }
                else {
                     AlertUtil.makeAlertDialog(this, "회원 정보 수정에 실패하였습니다")
                }
            }
            is RoomUtil.Error -> {
                AlertUtil.makeAlertDialog(this, "회원 정보 수정에 실패하였습니다")
            }
        }
    }
}