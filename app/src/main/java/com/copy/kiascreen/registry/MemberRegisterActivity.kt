package com.copy.kiascreen.registry

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.ActivityMemberRegisterBinding
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.login.LoginActivity
import com.copy.kiascreen.repeatOnStarted
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.AlertUtil
import com.copy.kiascreen.util.KeyboardScrollUtil
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class MemberRegisterActivity : BaseActivity<ActivityMemberRegisterBinding, RegisterViewModel>(TransitionMode.HORIZON) {
    override val viewModel : RegisterViewModel by viewModels()

    private lateinit var idEditText: AppCompatEditText
    private lateinit var mailEditText : AppCompatEditText
    private lateinit var pwdEditText : AppCompatEditText
    private lateinit var pwdChkEditText : AppCompatEditText
    private lateinit var idDupChkBtn : AppCompatButton
    private lateinit var registerBtn : AppCompatButton
    private lateinit var dupChkText : TextView
    private lateinit var chkPwdText : TextView
    private lateinit var toolbar : MaterialToolbar
    private lateinit var mailAddrEditText : AppCompatEditText
    private lateinit var keyboardUtil : KeyboardScrollUtil

    private val inputFilterId by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            }
            else {
                return@InputFilter null
            }
        }
    }

    private val inputFilterId2 by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z0-9]*$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    private val mailRegx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

    private val inputFilterMail by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*.]*$")
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

    private var isSignEnabled = false
    private var isDuplicatedId = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initView()
        initToolbar()
        setKeyDownListener()
        setPwdInputFilter()
        setSpinnerAdapter()
        setOnKeyListener()

        setOnDupIdChkBtn()
        setRegisterBtn()

        repeatOnStarted {
            viewModel.regitSharedFlow.collect {
                handleRegisterResult(it)
            }
        }

        repeatOnStarted {
            viewModel.userSharedFlow.collect {
                handleDupChkResult(it)
            }
        }
    }

    override fun getDataFromIntent() {}

    override fun initView() {
        idEditText = binding.inputIdEt
        pwdEditText = binding.inputPwdEt
        pwdChkEditText = binding.inputPwdChkEt
        idDupChkBtn = binding.idDupChkBtn
        registerBtn = binding.registerBtn
        dupChkText = binding.dupChkTv
        chkPwdText = binding.pwdChkTv
        mailEditText = binding.inputEmailEt
        toolbar = binding.regitToolbar
        mailAddrEditText = binding.inputEmailDomainEt

        keyboardUtil = KeyboardScrollUtil(window, onShowKeyboard =  { keyboardHeight -> binding.scrollView.run {
                smoothScrollTo(0, scrollY + keyboardHeight)
            }
        })
    }

    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivityMemberRegisterBinding.inflate(layoutInflater)

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gohome_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { finish() }
            R.id.mypage_go_home -> {
                Intent(this, BuildCompActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 비밀번호, 비밀번호 입력 창의 keydown 리스너
    private fun setKeyDownListener() {
        idEditText.filters = arrayOf(inputFilterId)

        pwdChkEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                chkPwd()
            }

            override fun afterTextChanged(s: Editable?) {
                chkPwd()
            }
        })

        idEditText.addTextChangedListener(object : TextWatcher  {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("registerTest", "before text chaned called count = $count")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("registerTest", "onTextChanged  chaned called count = $count")
                if (!isDuplicatedId)  {
                    isDuplicatedId = true
                    binding.dupChkTv.text = "아이디 중복 확인을 해주세요"
                    binding.dupChkTv.setTextColor(resources.getColor(R.color.blue))
                }

                if (count == 0 && idEditText.text!!.isEmpty()) {
                    idEditText.filters = arrayOf(inputFilterId)
                }
                else if (count >= 1 || idEditText.text!!.isNotEmpty()) {
                    idEditText.filters = arrayOf(inputFilterId2)
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        mailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0 && mailEditText.text!!.isEmpty()) {
                    mailEditText.filters = arrayOf(inputFilterId)
                }
                else if (count >= 1) {
                    mailEditText.filters = arrayOf(inputFilterId2)
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })

        mailAddrEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mailAddrEditText.text!!.isNotEmpty()) {
                    mailAddrEditText.filters = arrayOf(mailDomainFilter)
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
    }

    private fun chkPwd() {
        if (pwdEditText.text.toString().equals(pwdChkEditText.text.toString())) {
            chkPwdText.text = "비밀번호가 일치합니다"
            chkPwdText.setTextColor(resources.getColor(R.color.blue))
            isSignEnabled = true
        }
        else {
            chkPwdText.text = "비밀번호가 일치하지 않습니다"
            chkPwdText.setTextColor(resources.getColor(R.color.red))
            isSignEnabled = false
        }
    }

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

    private fun setSpinnerAdapter() {
        var mAdapter = ArrayAdapter.createFromResource(this,
            R.array.email_domain_array,
            R.layout.item_spinner
        )
        mAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.mailSpinner.adapter = mAdapter

        binding.mailSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var mail = ""
            var cnt = 0
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("spinnerTest", "position = $position")
                when(position) {
                    0 -> {
                        mail = ""
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
                /*
                if (mailEditText.text!!.isNotEmpty() && mail.isNotBlank()) {
                    Log.d("spinnerTest", "mail = $mail mailedittxt = ${mailEditText.text.toString()}")
                    var txt = mailEditText.text.toString().trim()
                    if (txt.indexOf('@') != -1) {
                        // 이미 존재하는 경우, @부터 지워줘야 함
                        txt = txt.substring(0, txt.indexOf('@'))
                    }
                    txt +=mail
                    Log.d("spinnerTest", "txt = $txt")
                    mailEditText.setText("$txt")
                    mailEditText.apply {
                        this.setText("$txt")
                        this.setSelection(this.text!!.length)
                        this.requestFocus()
                    }
                }

                 */

                if (mailEditText!!.text!!.isNotEmpty() && cnt > 0) {
                    mailAddrEditText.setText(mail)
                }
                else if (mailEditText.text!!.isEmpty() && cnt > 0) {
                    mailEditText.setText(mail)
                }
                cnt++
                mail=""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun setOnDupIdChkBtn () {
        idDupChkBtn.setOnClickListener {
            chkDupIdChk()
        }
    }

    private fun chkDupIdChk() {
        Log.d("regitTest", "id = ${idEditText.text!!.toString()}")
        if (idEditText.text!!.isNotBlank()) {
            var id = idEditText.text.toString()
            viewModel.chkDupId(id)
        }
    }

    private fun setRegisterBtn() {
        binding.registerBtn.setOnClickListener {
            chkRegister()
        }
    }

    private fun chkRegister() {
        if (isMailChked() && !isDuplicatedId && isSignEnabled) {
            Log.d("regitTest", "chkRegister test = ${binding.inputNameEt.text.toString()}, id =${idEditText.text.toString()}")
            // 회원 가입 가능
            val user = User(idEditText.text.toString(), pwdEditText.text.toString(), binding.inputNameEt.text.toString() , mailEditText.text.toString())

            viewModel.regitMember(user)
        }
        else {
            if (!isMailChked()) {
                // 이메일 다시 입력
                AlertUtil.makeAlertDialog(this, "이메일 입력을 확인해 주세요")
            }
            else if (isDuplicatedId) {
                // 아이디 중복 확인
                AlertUtil.makeAlertDialog(this, "아이디 중복을 확인해 주세요")
            }
            else {
                // 비밀번호 확인 다름
                AlertUtil.makeAlertDialog(this, "비밀번호가 일치하지 않습니다")
            }
        }
    }

    private fun isMailChked() : Boolean {
        var mail = (mailEditText.text.toString()+'@'+ mailAddrEditText.text.toString()).trim()
        val pattern = Pattern.compile(mailRegx)
        return if (mail.isNullOrEmpty() || mail.isNullOrBlank()) {
            false
        } else {
            val matcher = pattern.matcher(mail)
            Log.d("regitTest", "${matcher.matches()}")
            matcher.matches()
        }
    }

    private fun handleRegisterResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("regitTest", "onLoading!!")
            is RoomUtil.Success -> {
                Toast.makeText(this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show()
                Intent(this, LoginActivity::class.java).apply {
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(this)
                    finish()
                }

            }
            is RoomUtil.Error -> {
                Log.d("regitTest", "error!! ${result.error.toString()}")
                AlertUtil.makeAlertDialog(this, "회원가입에 실패하였습니다 잠시 후 다시 시도해 주세요")
            }
        }
    }

    private fun handleDupChkResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("regitTest", "onLoading!!")
            is RoomUtil.Success -> {
                isDuplicatedId = if (result.successOrNull() != null) {
                    AlertUtil.makeAlertDialog(this, "중복된 아이디 입니다!")
                    true
                } else {
                    AlertUtil.makeAlertDialog(this, "사용 가능한 아이디 입니다!")
                    binding.dupChkTv.apply {
                        this.text = "사용 가능한 아이디 입니다"
                        this.setTextColor(resources.getColor(R.color.blue))
                    }
                    false
                }
            }
            is RoomUtil.Error -> Log.d("regitTest", "Error !! ${result.error}")
        }
    }

    private fun setOnKeyListener() {
        idEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                chkDupIdChk()
                true
            }
            else {
                false
            }

        }

        binding.inputNameEt.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                Log.d("regitTest", "binding.inputNameEt.setOnKeyListener")
                if (binding.inputNameEt.text!!.isNotEmpty()) {
                    Log.d("regitTest", "binding.inputNameEt.text!!.isNotEmpty()")
                    mailEditText.isFocusable = true
                    true
                }
            }
            false
        }

        binding.inputEmailEt.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                Log.d("regitTest", "binding.inputEmailEt.setOnKeyListener")
                if (binding.inputEmailEt.text!!.isNotEmpty()) {
                    binding.mailSpinner.isFocusable = true
                    binding.mailSpinner.requestFocus()
                    binding.mailSpinner.performClick()
                    true
                }
            }
            false
        }
    }

    override fun onBackPressed() {
        // 회원가입 정보가 다 날라간다는걸 명시
        AlertUtil.makeAlertDialogIntent(this, "기입한 정보는 유지되지 않습니다 정말 종료하시겠습니까?")
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardUtil.detachKeyboardListener()
    }
}