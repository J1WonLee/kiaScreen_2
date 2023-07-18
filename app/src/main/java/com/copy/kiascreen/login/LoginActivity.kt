package com.copy.kiascreen.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.EditText
import androidx.activity.viewModels
import com.copy.kiascreen.registry.MemberRegisterActivity
import com.copy.kiascreen.R
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.databinding.ActivityLoginBinding
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.repeatOnStarted
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.AlertUtil
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(TransitionMode.HORIZON) {
    override val viewModel: LoginViewModel by viewModels()

    private lateinit var idInputEdit : EditText
    private lateinit var pwdInputEdit : EditText
    private lateinit var toolbar : MaterialToolbar

    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        repeatOnStarted {
            viewModel.logInSharedFlow.collect {
                handleLoginResult(it)
            }
        }
        setPwdEnterListener()

        setLoginBtnListener()
        setFindClickEvent()
    }

    override fun getDataFromIntent() {}

    override fun initView() {
        idInputEdit = binding.loginIdEditText
        pwdInputEdit = binding.loginPwdEditText
        toolbar = binding.loginToolbar
    }

    private fun initToolbar () {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gohome_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setLoginBtnListener() {
        binding.loginBtn.setOnClickListener {
            login()
        }

        binding.registerTv.setOnClickListener {
            Intent(this, MemberRegisterActivity::class.java).apply {
                startActivity(this)
            }

        }
    }

    private fun setPwdEnterListener() {
        pwdInputEdit.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                login()
                true
            }else {
                false
            }
        })
    }

    private fun login() {
        if (idInputEdit.text.isNotEmpty() && pwdInputEdit.text.isNotEmpty()) {
            viewModel.login(idInputEdit.text.toString(), pwdInputEdit.text.toString())
        }
    }

    private fun handleLoginResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("loginTest", "onLoading!!")
            is RoomUtil.Success -> {
                if (result.successOrNull() != null) {
                    KiaSampleApplication.prefs.loginFlag = true
                    Intent(this, BuildCompActivity::class.java).apply {
                        Log.d("loginTEst", "from LoginActivity result = ${result.successOrNull()}")
//                        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra("user", result.data as User)
                        startActivity(this)
                        finish()
                    }
                }
                else {
                    AlertUtil.makeAlertDialog(this, "비밀번호와 아이디가 일치하지 않습니다")
                }

            }
            is RoomUtil.Error -> {
                AlertUtil.makeAlertDialog(this, "비밀번호와 아이디가 일치하지 않습니다")
            }
        }
    }

    private fun setFindClickEvent() {
        binding.findIdTv.setOnClickListener {
            initFindIdFragment(true)
        }

        binding.findPwdTv.setOnClickListener {
            initFindIdFragment(false)
        }
    }

    private fun initFindIdFragment(isId : Boolean) {
        if (isId) {
            FindIdFragment().show(
                supportFragmentManager, null
            )
        }
        else {
            FindPwdFragment().show(
                supportFragmentManager, null
            )
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            // main으로 돌려보낸다
            Intent(this@LoginActivity, BuildCompActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        else {
            super.onBackPressed()
        }
    }
}